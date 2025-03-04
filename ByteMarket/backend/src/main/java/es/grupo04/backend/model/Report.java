package es.grupo04.backend.model;

import jakarta.persistence.*;

@Entity
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String reason;
    
    private String description;

    @ManyToOne
    private Product product;

    @ManyToOne 
    private User user;

    public Report() {
    }

    public Report(String reason, String description, Product product, User user) {
        this.reason = reason;
        this.description = description;
        this.product = product;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
