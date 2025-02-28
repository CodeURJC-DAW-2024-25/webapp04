package es.grupo04.backend.service;

import es.grupo04.backend.model.Chat;
import es.grupo04.backend.model.Product;
import es.grupo04.backend.model.User;
import es.grupo04.backend.repository.ChatRepository;
import es.grupo04.backend.repository.ProductRepository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ProductRepository productRepository;

    public Chat createChat(User buyer, User seller, Long productId) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product != null) {
            Chat chat = new Chat(buyer, seller, product);
            return chatRepository.save(chat);
        }
        return null;
    }

    public Chat findChat(User buyer, User seller, Long productId) {
        return chatRepository.findByUsersAndProduct(buyer, seller, productId);
    }

    public Optional<Chat> findChatById(Long chatId) {
        return chatRepository.findById(chatId);
    }

    

}
