package es.grupo04.backend.service;

import es.grupo04.backend.dto.ChatDTO;
import es.grupo04.backend.dto.ChatMapper;
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

    @Autowired
    private ChatMapper chatMapper;

    public ChatDTO createChat(User buyer, User seller, Long productId) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product != null) {
            Chat existingChat = chatRepository.findByUsersAndProduct(buyer, seller, productId);
            if (existingChat != null) {
                return chatMapper.toDTO(existingChat);
            }
            Chat chat = new Chat(buyer, seller, product);
            Chat savedChat = chatRepository.save(chat);
            return chatMapper.toDTO(savedChat);
        }
        return null;
    }

    public ChatDTO findChat(User buyer, User seller, Long productId) {
        Chat chat = chatRepository.findByUsersAndProduct(buyer, seller, productId);
        return chat != null ? chatMapper.toDTO(chat) : null;
    }

    public Optional<ChatDTO> findChatById(Long chatId) {
        return chatRepository.findById(chatId).map(chatMapper::toDTO);
    }
}
