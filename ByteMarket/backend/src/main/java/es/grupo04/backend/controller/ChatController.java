package es.grupo04.backend.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import es.grupo04.backend.model.Chat;
import es.grupo04.backend.model.Message;
import es.grupo04.backend.model.Product;
import es.grupo04.backend.model.User;
import es.grupo04.backend.service.ChatService;
import es.grupo04.backend.service.MessageService;
import es.grupo04.backend.service.ProductService;
import es.grupo04.backend.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.csrf.CsrfToken;

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

    @ModelAttribute
    public void addAttributes(Model model, HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();

        if (principal != null) {

            User user = userservice.findByMail(principal.getName()).get();

            model.addAttribute("logged", true);
            model.addAttribute("userName", user.getName());
            model.addAttribute("admin", request.isUserInRole("ADMIN"));
            model.addAttribute("user", user);

        } else {
            model.addAttribute("logged", false);
        }
    }
    
    @GetMapping("/chat")
    public String HomeChat(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userservice.findByMail(userDetails.getUsername()).orElse(null);
        List<Chat> chats = user.getAllChats();
        List<Chat> checkedChats = new ArrayList<>();
        for (Chat chat : chats) {
            chat.isSelling(user);
            checkedChats.add(chat);
        }
        model.addAttribute("chats", checkedChats);
        model.addAttribute("title", "Chats");
        return "chat_template";
    }

    @PostMapping("/chat/new/{productId}")
    public String newChat(@PathVariable Long productId, @AuthenticationPrincipal UserDetails userDetails, Model model,
            RedirectAttributes redirectAttributes) {
        Product product = productservice.findById(productId).orElse(null);
        User seller = product.getOwner();
        User user = userservice.findByMail(userDetails.getUsername()).orElse(null);
        Chat existingChat = chatservice.findChat(user, seller, productId);

        if (existingChat != null) {
            return "redirect:/chat/" + existingChat.getId();
        } else {
            existingChat = chatservice.createChat(user, seller, productId);
            return "redirect:/chat/" + existingChat.getId();
        }
    }

    @GetMapping("/chat/{chatId}")
    public String privateChat(@PathVariable Long chatId, @AuthenticationPrincipal UserDetails userDetails,
            Model model) {

        User user = userservice.findByMail(userDetails.getUsername()).orElse(null);
        List<Chat> chats = user.getAllChats();
        List<Chat> checkedChats = new ArrayList<>();
        for (Chat chat : chats) {
            chat.isSelling(user);
            checkedChats.add(chat);
        }
        model.addAttribute("chats", checkedChats);
        Chat existingChat = chatservice.findChatById(chatId).orElse(null);
        Product product = existingChat.getProduct();

        model.addAttribute("current_chat", existingChat);
        model.addAttribute("current_chat_name", product.getName());
        model.addAttribute("messages", existingChat != null ? existingChat.getMessages() : null);
        model.addAttribute("chats", user.getAllChats());
        model.addAttribute("messages", existingChat.getMessages());
        model.addAttribute("title", "Chats");
        model.addAttribute("current_chat_name", product.getName());
        model.addAttribute("messages", existingChat != null ? existingChat.getMessages() : null);

        return "chat_template";
    }

    @PostMapping("/chat/{chatId}/send")
    public String sendMessage(@PathVariable Long chatId,
            @RequestParam String message,
            @AuthenticationPrincipal UserDetails userDetails) {

        User sender = userservice.findByMail(userDetails.getUsername()).orElse(null);
        Chat chat = chatservice.findChatById(chatId).orElse(null);

        if (sender != null && chat != null) {
            Message newMessage = new Message(message, sender, chat);
            messageservice.save(newMessage);
        }

        return "redirect:/chat/" + chatId;
    }
    

}
