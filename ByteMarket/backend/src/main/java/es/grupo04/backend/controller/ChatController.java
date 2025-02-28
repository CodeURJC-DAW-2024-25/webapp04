package es.grupo04.backend.controller;

import java.security.Principal;
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
import es.grupo04.backend.model.Product;
import es.grupo04.backend.model.User;
import es.grupo04.backend.service.ChatService;
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


    @ModelAttribute
    public void addAttributes(Model model, HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();

        if (principal != null) {

            model.addAttribute("logged", true);
            model.addAttribute("userName", userservice.findByMail(principal.getName()).get().getName());    
            model.addAttribute("admin", request.isUserInRole("ADMIN"));

        } else {
            model.addAttribute("logged", false);
        }
    }

    @GetMapping("/chat")
    public String HomeChat(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userservice.findByMail(userDetails.getUsername()).orElse(null);   
        model.addAttribute("chats", user.getAllChats());
        model.addAttribute("title", "Chats");      
        return "chat_template";
    }

    @PostMapping("/chat/new/{productId}")
    public String newChat(@PathVariable Long productId, @AuthenticationPrincipal UserDetails userDetails, Model model, RedirectAttributes redirectAttributes) {
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
    public String privateChat(@PathVariable Long chatId, @AuthenticationPrincipal UserDetails userDetails, Model model) {  

        User user = userservice.findByMail(userDetails.getUsername()).orElse(null); 
        Chat existingChat = chatservice.findChatById(chatId).orElse(null);
        Product product = existingChat.getProduct(); 

        model.addAttribute("current_chat", existingChat);
        model.addAttribute("current_chat_name", product.getName());
        model.addAttribute("messages", existingChat != null ? existingChat.getMessages() : null);
        model.addAttribute("chats", user.getAllChats());
        model.addAttribute("title", "Chats");
        model.addAttribute("current_chat_name", product.getName());
        model.addAttribute("messages", existingChat != null ? existingChat.getMessages() : null);

        return "chat_template"; 
    }

}
