package es.grupo04.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.grupo04.backend.dto.ChatDTO;
import es.grupo04.backend.model.Chat;
import es.grupo04.backend.model.Message;
import es.grupo04.backend.model.User;
import es.grupo04.backend.repository.ChatRepository;
import es.grupo04.backend.repository.MessageRepository;

@Service
public class MessageService {
    
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChatRepository chatRepository;

    public void save(Message message) {
        messageRepository.save(message);
    }

    public Message createMessage(String messageContent, User sender, ChatDTO chatDTO) {
        Chat chat = chatRepository.findById(chatDTO.id())
                .orElseThrow(() -> new RuntimeException("Chat no encontrado"));

        Message newMessage = new Message(messageContent, sender, chat);
        return messageRepository.save(newMessage);
    }

}
