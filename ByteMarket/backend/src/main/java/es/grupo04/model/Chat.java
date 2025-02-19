package es.grupo04.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Chat {
    
    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User userBuyer;
    @ManyToOne
    private User userSeller;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages;

    @ManyToOne
    private Product product;
    
    public Chat() {
    }
    
    public Chat(User user1, User user2) {
        this.userBuyer = user1;
        this.userSeller = user2;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public User getUserBuyer() {
        return userBuyer;
    }
    
    public void setUserBuyer(User user1) {
        this.userBuyer = user1;
    }
    
    public User getUserSeller() {
        return userSeller;
    }
    
    public void setUserSeller(User user2) {
        this.userSeller = user2;
    }
    
    public List<Message> getMessages() {
        return messages;
    }
    
    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
    
    public void addMessage(Message message) {
        this.messages.add(message);
    }
    
    public void removeMessage(Message message) {
        this.messages.remove(message);
    }
    
    public void clearMessages() {
        this.messages.clear();
    }
}
