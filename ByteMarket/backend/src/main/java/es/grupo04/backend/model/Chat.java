package es.grupo04.backend.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "buyer_id", nullable = false)
    private User userBuyer;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private User userSeller;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private boolean selling;

    public Chat() {
    }

    public Chat(User user1, User user2, Product product) {
        this.userBuyer = user1;
        this.userSeller = user2;
        this.product = product; 
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

    public void setUserBuyer(User userBuyer) {
        this.userBuyer = userBuyer;
    }

    public User getUserSeller() {
        return userSeller;
    }

    public void setUserSeller(User userSeller) {
        this.userSeller = userSeller;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public void addMessage(Message message) {
        this.messages.add(message);
        message.setChat(this);
    }

    public void removeMessage(Message message) {
        this.messages.remove(message);
        message.setChat(null);
    }

    public void clearMessages() {
        this.messages.forEach(msg -> msg.setChat(null));
        this.messages.clear();
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void isSelling(User user){
        if(user.equals(this.userSeller)){
            this.selling = true;
        }else{
            this.selling = false;
        }
    }
}
