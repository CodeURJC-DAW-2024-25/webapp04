package es.grupo04.backend.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(columnDefinition = "TEXT")
	private String description;

    @ManyToOne 
    private User reviewOwner;
    
    @ManyToOne 
    private User reviewedUser;

    @Column(nullable = false)
    private Integer rating;


    public Review() {
    }

    public Review(User reviewOwner, User reviewedUser, String description, Integer rating) {
        this.reviewOwner = reviewOwner;
        this.reviewedUser = reviewedUser;
        this.description = description;
        this.rating = rating;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getreviewOwner() {
        return reviewOwner;
    }

    public void setreviewOwner(User user) {
        this.reviewOwner = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("La valoración debe estar entre 1 y 5.");
        }
        this.rating = rating;
    }

    public User getReviewedUser() {
        return reviewedUser;
    }

    public void setReviewedUser(User reviewedUser) {
        this.reviewedUser = reviewedUser;
    }
}
