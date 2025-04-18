package es.grupo04.backend.controller;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.grupo04.backend.dto.ChatDTO;
import es.grupo04.backend.dto.MessageDTO;
import es.grupo04.backend.dto.NewChatDTO;
import es.grupo04.backend.dto.NewMessageDTO;
import es.grupo04.backend.dto.ProductDTO;
import es.grupo04.backend.dto.UserBasicDTO;
import es.grupo04.backend.service.ChatService;
import es.grupo04.backend.service.MessageService;
import es.grupo04.backend.service.ProductService;
import es.grupo04.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/chats")
public class ChatRestController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ChatService chatService;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;


    @Operation (summary= "Retrieve a list of all chats of the authenticated user")
    @GetMapping
    public List<ChatDTO> getUserChats(HttpServletRequest request, @RequestParam(required = false) String role) {
        Principal principal = request.getUserPrincipal();
        UserBasicDTO userDTO = userService.findByMail(principal.getName()).get();
        List<ChatDTO> chats = chatService.findChatsByUserId(userDTO.id());
        if ("buyer".equalsIgnoreCase(role)) {
            chats = chats.stream()
                    .filter(chat -> chat.userBuyer().id().equals(userDTO.id()))
                    .toList();
        } else if ("seller".equalsIgnoreCase(role)) {
            chats = chats.stream()
                    .filter(chat -> chat.userSeller().id().equals(userDTO.id()))
                    .toList();
        }
        return chats;
    }

    @Operation (summary= "Create a new chat by the product ID")
    @PostMapping
    public ResponseEntity<ChatDTO> createChat(@RequestBody NewChatDTO newchatDTO, HttpServletRequest request) throws IOException {
        Principal principal = request.getUserPrincipal();
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        UserBasicDTO userDTO = userService.findByMail(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        ProductDTO product = productService.findById(newchatDTO.productID())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));

        if (userDTO.id().equals(product.owner().id())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No puedes crear un chat con el propietario del producto");
        }

        ChatDTO existingChat = chatService.findChat(userDTO, product.owner(), newchatDTO.productID());
        if (existingChat != null) {
            return ResponseEntity.ok(existingChat);
        } else {
            ChatDTO newChat = chatService.createChat(userDTO, product.owner(), newchatDTO.productID());
            URI location = ServletUriComponentsBuilder
            .fromCurrentRequestUri()
            .replacePath(String.format("/api/v1/users/me/chats/%d", newChat.id()))
            .build()
            .toUri();
            return ResponseEntity.created(location).body(newChat);
        }
    }

    @Operation (summary= "Retrieve a chat by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ChatDTO> getChat(@PathVariable Long id, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        UserBasicDTO userDTO = userService.findByMail(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        ChatDTO chat = chatService.findChatById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chat no encontrado"));

        if (!chat.userBuyer().equals(userDTO) && !chat.userSeller().equals(userDTO)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permisos para ver este chat");
        }

        return ResponseEntity.ok(chat);
    }

    @Operation (summary= "Send a message in a chat by its ID")
    @PostMapping("/{id}/messages")
    public ResponseEntity<?> sendMessage(@PathVariable Long id, @RequestBody NewMessageDTO message, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        UserBasicDTO sender = userService.findByMail(principal.getName()).get();

        ChatDTO chat = chatService.findChatById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chat no encontrado"));

        if (!chat.userBuyer().equals(sender) && !chat.userSeller().equals(sender)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permisos para ver este chat");
        }

        MessageDTO messageDTO = messageService.createMessage(message.text(), sender, chat);
        return ResponseEntity.ok(messageDTO);
    }
}
