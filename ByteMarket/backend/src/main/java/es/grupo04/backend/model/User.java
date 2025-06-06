package es.grupo04.backend.model;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import java.time.Year;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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

    @Column(unique = true)
    private String mail;
    
    private List<String> roles;
    private String name;
    private String encodedPassword;

    @Lob
    private Blob imageFile;
    private boolean image = false;

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
    private List<Product> favoriteProducts;

    @OneToMany(mappedBy = "userBuyer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chat> chatsAsBuyer = new ArrayList<>();

    @OneToMany(mappedBy = "userSeller", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chat> chatsAsSeller = new ArrayList<>();

    private Integer creationYear;

    private String iframe;


    public User(){
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

    public List<Product> getFavoriteProducts() {
        return favoriteProducts;
    }
    public void setFavoriteProducts(List<Product> favoriteProducts) {
        this.favoriteProducts = favoriteProducts;
    }

    public List<Purchase> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<Purchase> purchases) {
        this.purchases = purchases;
    }

    public List<Purchase> getSales() {
        return sales;
    }

    public void setSales(List<Purchase> sales) {
        this.sales = sales;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public void addChatAsBuyer(Chat chat) {
        if (!this.chatsAsBuyer.contains(chat)) {
            this.chatsAsBuyer.add(chat);
        }
    }

    public List<Chat> getChatsAsSeller() {
        return chatsAsSeller;
    }

    public List<Chat> getAllChats() {
        List<Chat> allChats = new ArrayList<>();
        allChats.addAll(chatsAsBuyer);
        allChats.addAll(chatsAsSeller);
        return allChats;
    }

    public String getIframe() {
        return iframe;
    }

    public void setIframe(String iframe) {
        this.iframe = iframe;
    }

    public List<Chat> getChatsAsBuyer() {
        return chatsAsBuyer;
    }

    public void addPurchase(Purchase purchase) {
        this.purchases.add(purchase);
    }

    public void addSale(Purchase purchase) {
        this.sales.add(purchase);
    }
    public Long getId() {
        return id;
    }
    public List<Review> getPostedReviews() {
        return postedReviews;
    }

    public void setPostedReviews(List<Review> postedReviews) {
        this.postedReviews = postedReviews;
    }

    public List<Product> getProducts() {
        return products;
    }
}
