package es.grupo04.backend.service;

import es.grupo04.backend.dto.ChatDTO;
import es.grupo04.backend.dto.ChatMapper;
import es.grupo04.backend.dto.UserBasicDTO;
import es.grupo04.backend.model.Chat;
import es.grupo04.backend.model.Product;
import es.grupo04.backend.model.User;
import es.grupo04.backend.repository.ChatRepository;
import es.grupo04.backend.repository.ProductRepository;
import es.grupo04.backend.repository.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatMapper chatMapper;

    public ChatDTO createChat(UserBasicDTO buyerDTO, UserBasicDTO sellerDTO, Long productId) {
        Optional<User> buyerOptional = userRepository.findById(buyerDTO.id());
        User buyer = buyerOptional.orElseThrow(() -> new RuntimeException("Buyer not found"));

        Optional<User> sellerOptional = userRepository.findById(sellerDTO.id());
        User seller = sellerOptional.orElseThrow(() -> new RuntimeException("Seller not found"));
        
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

    public ChatDTO findChat(UserBasicDTO buyerDTO, UserBasicDTO sellerDTO, Long productId) {
        Optional<User> buyerOptional = userRepository.findById(buyerDTO.id());
        User buyer = buyerOptional.orElseThrow(() -> new RuntimeException("Buyer not found"));

        Optional<User> sellerOptional = userRepository.findById(sellerDTO.id());
        User seller = sellerOptional.orElseThrow(() -> new RuntimeException("Seller not found"));

        Chat chat = chatRepository.findByUsersAndProduct(buyer, seller, productId);
        return chat != null ? chatMapper.toDTO(chat) : null;
    }

    public Optional<ChatDTO> findChatById(Long chatId) {
        return chatRepository.findById(chatId).map(chatMapper::toDTO);
    }   
    
    public List<ChatDTO> findChatsByUserId(Long userId) {
    Optional<User> userOptional = userRepository.findById(userId);
    User user = userOptional.orElseThrow(() -> new RuntimeException("User not found"));

    List<Chat> chats = chatRepository.findByUserBuyerOrUserSeller(user, user);
    return chats.stream()
                .map(chatMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<ChatDTO> getChatsAsSeller(UserBasicDTO user, List<ChatDTO> chatsDTO) {
        return chatsDTO.stream()
                .filter(chatDTO -> chatDTO.userSeller().equals(user))
                .collect(Collectors.toList());
    }

    public ChatDTO isUserSeller(UserBasicDTO userDTO, ChatDTO chatDTO) {
        Optional<User> userOptional = userRepository.findById(userDTO.id());
        if(!userOptional.isPresent()){
            throw new NoSuchElementException("User not found");
        }
        
        Chat chat = chatRepository.findById(chatDTO.id())
            .orElseThrow(() -> new NoSuchElementException("Chat not found"));
        
        ChatDTO updatedChatDTO = chatMapper.toDTO(chat);
        
        return updatedChatDTO;
    }
    

    
}
