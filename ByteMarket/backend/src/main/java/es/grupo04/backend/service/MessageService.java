package es.grupo04.backend.service;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.grupo04.backend.dto.ChatDTO;
import es.grupo04.backend.dto.UserBasicDTO;
import es.grupo04.backend.model.Chat;
import es.grupo04.backend.model.Message;
import es.grupo04.backend.model.User;
import es.grupo04.backend.repository.ChatRepository;
import es.grupo04.backend.repository.MessageRepository;
import es.grupo04.backend.repository.UserRepository;

@Service
public class MessageService {
    
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UserRepository userRepository;

    public void save(Message message) {
        messageRepository.save(message);
    }

    public Message createMessage(String messageContent, UserBasicDTO senderDTO, ChatDTO chatDTO) {
        Chat chat = chatRepository.findById(chatDTO.id())
                .orElseThrow(() -> new NoSuchElementException());
        Optional<User> userOptional = userRepository.findById(senderDTO.id());
        User sender = userOptional.orElseThrow(() -> new NoSuchElementException());

        Message newMessage = new Message(messageContent, sender, chat);
        return messageRepository.save(newMessage);
    }

}
