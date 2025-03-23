package es.grupo04.backend.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import es.grupo04.backend.dto.ChatDTO;
import es.grupo04.backend.dto.ProductBasicDTO;
import es.grupo04.backend.dto.ProductDTO;
import es.grupo04.backend.dto.PurchaseDTO;
import es.grupo04.backend.dto.UserBasicDTO;
import es.grupo04.backend.service.ChatService;
import es.grupo04.backend.service.MessageService;
import es.grupo04.backend.service.ProductService;
import es.grupo04.backend.service.PurchaseService;
import es.grupo04.backend.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

@Controller
public class ChatController {

    @Autowired
    private ProductService productservice;

    @Autowired
    private ChatService chatservice;

    @Autowired
    private UserService userservice;

    @Autowired
    private MessageService messageservice;

    @Autowired
    private PurchaseService purchaseservice;

    @ModelAttribute
    public void addAttributes(Model model, HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();

        if (principal != null) {

            UserBasicDTO user = userservice.findByMail(principal.getName()).get();

            model.addAttribute("logged", true);
            model.addAttribute("userName", user.name());
            model.addAttribute("admin", request.isUserInRole("ADMIN"));
            model.addAttribute("user", user);

        } else {
            model.addAttribute("logged", false);
        }
    }

    @GetMapping("/chat")
    public String HomeChat(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        UserBasicDTO user = userservice.findByMail(userDetails.getUsername()).orElse(null);
        List<ChatDTO> chats = chatservice.findChatsByUserId(user.id());
        List<ChatDTO> checkedChats = new ArrayList<>();
        for (ChatDTO chat : chats) {
            ChatDTO chatUpdated = chatservice.isUserSeller(user, chat);
            checkedChats.add(chatUpdated);
        }

        model.addAttribute("chats", checkedChats);
        model.addAttribute("isSeller", chatservice.getChatsAsSeller(user, chats));
        model.addAttribute("title", "Chats");
        return "chat_template";
    }

    @PostMapping("/chat/new/{productId}")
    public String newChat(@PathVariable Long productId, @AuthenticationPrincipal UserDetails userDetails, Model model,
            RedirectAttributes redirectAttributes) {
        ProductDTO product = productservice.findById(productId).orElse(null);
        UserBasicDTO seller = product.owner();
        UserBasicDTO user = userservice.findByMail(userDetails.getUsername()).orElse(null);
        ChatDTO existingChat = chatservice.findChat(user, seller, productId);

        if (existingChat != null) {
            return "redirect:/chat/" + existingChat.id();
        } else {
            existingChat = chatservice.createChat(user, seller, productId);
            return "redirect:/chat/" + existingChat.id();
        }
    }

    @GetMapping("/chat/{chatId}")
    public String privateChat(@PathVariable Long chatId, @AuthenticationPrincipal UserDetails userDetails,
            Model model) {

        UserBasicDTO user = userservice.findByMail(userDetails.getUsername()).orElse(null);
        List<ChatDTO> chats = chatservice.findChatsByUserId(user.id());
        List<ChatDTO> checkedChats = new ArrayList<>();
        for (ChatDTO chat : chats) {
            ChatDTO chatUpdated = chatservice.isUserSeller(user, chat);
            checkedChats.add(chatUpdated);
        }
        ChatDTO existingChat = chatservice.findChatById(chatId).orElse(null);
        if (existingChat == null) {
            model.addAttribute("message", "El chat no existe");
            return "error";
        }
        if (existingChat.userBuyer().id() != user.id() && existingChat.userSeller().id() != user.id()) {
            model.addAttribute("message", "No tienes acceso a este chat");
            return "error";
        }

        ProductBasicDTO product = existingChat.product();

        model.addAttribute("current_chat", existingChat);
        model.addAttribute("current_chat_name", product.name());
        model.addAttribute("messages", existingChat != null ? existingChat.messages() : null);
        model.addAttribute("chats", checkedChats);
        model.addAttribute("messages", existingChat.messages());
        model.addAttribute("title", "Chats");
        model.addAttribute("current_chat_name", product.name());
        model.addAttribute("messages", existingChat != null ? existingChat.messages() : null);
        model.addAttribute("isSeller", chatservice.isUserSeller(user, existingChat));

        return "chat_template";
    }

    @PostMapping("/chat/{chatId}/send")
    public String sendMessage(@PathVariable Long chatId,
            @RequestParam String message,
            @AuthenticationPrincipal UserDetails userDetails) {

        UserBasicDTO sender = userservice.findByMail(userDetails.getUsername()).orElse(null);
        ChatDTO chat = chatservice.findChatById(chatId).orElse(null);

        if (sender != null && chat != null) {
            messageservice.createMessage(message, sender, chat);
        }

        return "redirect:/chat/" + chatId;
    }

    //Sell a product
    @PostMapping("/chat/sellScreen/{chatId}")
    public String confirmSell(@PathVariable Long chatId, Model model,
            @AuthenticationPrincipal UserDetails userDetails) {
        ChatDTO chat = chatservice.findChatById(chatId).orElse(null);
        if (chat == null) {
            return "redirect:/chat";
        }

        //Get the logged user an verify its the seller
        UserBasicDTO seller = userservice.findByMail(userDetails.getUsername()).orElse(null);
        if (seller == null || !chat.userSeller().equals(seller)) {
            return "redirect:/chat";
        }

        model.addAttribute("chat", chat);
        model.addAttribute("product", chat.product());
        model.addAttribute("buyer", chat.userBuyer());
        model.addAttribute("title", "Confirmar venta");
        return "sell_template";
    }

    // New Purchase
    @PostMapping("/chat/sell/{chatId}")
    public String sellProduct(@PathVariable Long chatId, Model model,
            @AuthenticationPrincipal UserDetails userDetails) {
        ChatDTO chat = chatservice.findChatById(chatId).orElse(null);
        if (chat == null) {
            model.addAttribute("message", "El chat no existe o no tienes acceso a este chat.");
            return "error"; 
        }

        //Check if the product was sold
        if (chat.product().sold()) {
            model.addAttribute("message", "El producto ya no está disponible");
            return "error"; 
        }

        //Get the seller and verify its the owner
        UserBasicDTO seller = userservice.findByMail(userDetails.getUsername()).orElse(null);
        if (seller == null || !chat.userSeller().equals(seller)) {
            model.addAttribute("message", "Solo el propietario del producto puede venderlo");
            return "error"; 
        }

        //Create Purchase
        PurchaseDTO purchase = purchaseservice.createPurchase(chat);
        if (purchase == null) {
            model.addAttribute("message", "No se ha podido realizar la venta del producto");
            return "error"; 
        }
        return "redirect:/";
    }

}
