package es.grupo04.backend.model;

import java.sql.Blob;
import java.util.List;
import java.time.Year;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

@Entity(name = "UserTable")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String mail;
    private List<String> roles;
    private String name;
    private String encodedPassword;

    @Lob
    private Blob imageFile;
    private boolean image;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products;

    @OneToMany(mappedBy = "reviewedUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;

    @OneToMany(mappedBy = "reviewOwner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> postedReviews;

    @OneToMany(mappedBy = "buyer")
    private List<Purchase> purchases;

    @OneToMany(mappedBy = "seller")
    private List<Purchase> sales;

    @ManyToMany
    private List<Product> favorites;

    @OneToMany(mappedBy = "userBuyer")
    private List<Chat> chatsPurchase;

    @OneToMany(mappedBy = "userSeller")
    private List<Chat> chatsSales;

    private Integer creationYear;


    public User() {
    }

    public User(String name, String encodedPassword, String mail, String... roles) {
        this.name = name;
        this.encodedPassword = encodedPassword;
        this.roles = List.of(roles);
        this.mail = mail;
        this.creationYear = Year.now().getValue();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEncodedPassword() {
        return encodedPassword;
    }

    public void setEncodedPassword(String password) {
        this.encodedPassword = password;
    }

    public List<String> getRoles() {
		return roles;
	}

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public Blob getImageFile() {
        return imageFile;
    }

    public void setImageFile(Blob image) {
        this.imageFile = image;
    }

    public boolean hasImage() {
        return this.image;
    }

    public void setImage(boolean image) {
        this.image = image;
    }

    public void setCreationYear(Integer creationYear) {
        this.creationYear = creationYear;
    }
    
    public Integer getCreationYear() {
        return creationYear;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
