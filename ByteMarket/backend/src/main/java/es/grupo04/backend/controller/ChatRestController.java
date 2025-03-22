package es.grupo04.backend.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import es.grupo04.backend.dto.ChatDTO;
import es.grupo04.backend.dto.ProductDTO;
import es.grupo04.backend.dto.PurchaseDTO;
import es.grupo04.backend.dto.UserBasicDTO;
import es.grupo04.backend.service.ChatService;
import es.grupo04.backend.service.MessageService;
import es.grupo04.backend.service.ProductService;
import es.grupo04.backend.service.PurchaseService;
import es.grupo04.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.server.ResponseStatusException;

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

    @Autowired
    private PurchaseService purchaseService;

    @GetMapping
    public List<ChatDTO> getUserChats(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        UserBasicDTO userDTO = userService.findByMail(principal.getName()).get();
        List<ChatDTO> chats = chatService.findChatsByUserId(userDTO.id());
        return chats;
    }


    @PostMapping("/{productId}")
    public ResponseEntity<ChatDTO> createChat(@PathVariable Long productId, HttpServletRequest request) throws IOException {
        Principal principal = request.getUserPrincipal();
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        UserBasicDTO userDTO = userService.findByMail(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        ProductDTO product = productService.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));

        if (userDTO.id().equals(product.owner().id())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No puedes crear un chat con el propietario del producto");
        }

        ChatDTO existingChat = chatService.findChat(userDTO, product.owner(), productId);
        if (existingChat != null) {
            return ResponseEntity.ok(existingChat);
        } else {
            ChatDTO newChat = chatService.createChat(userDTO, product.owner(), productId);
            return ResponseEntity.ok(newChat);
        }
    }


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

    @PostMapping("/{id}/send")
    public ResponseEntity<Void> sendMessage(@PathVariable Long id, @RequestParam String message, HttpServletRequest request) {
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

        messageService.createMessage(message, sender, chat);
        
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/sell")
    public ResponseEntity<Void> sellProduct(@PathVariable Long id, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        UserBasicDTO seller = userService.findByMail(principal.getName()).get();
        ChatDTO chat = chatService.findChatById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chat no encontrado"));

        if (chat.product().sold()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El producto ya está vendido");
        }

        if (!chat.userSeller().equals(seller)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permisos para vender este producto");
        }

        PurchaseDTO purchase = purchaseService.createPurchase(chat);
        if (purchase == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo completar la venta");
        }
        return ResponseEntity.ok().build();
    }
}
