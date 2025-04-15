package es.grupo04.backend.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;


@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String name;
    private Float price;
    private String category;
    
    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    private User owner;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;
    private boolean hasImage = false;
    
    @OneToOne
    private Image thumbnail;

    private boolean sold;
    
    private LocalDate publishDate = LocalDate.now();

    @ManyToMany
    private List<User> users;

    @ManyToMany (mappedBy = "favoriteProducts")
    private List<User> favoriteOfUser;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Report> reports;

    public Product() {
    }

    public Product(String name, String description, float price, String category, List<Image> images) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.images = images;
        this.hasImage = true;
        if(!images.isEmpty()){
            this.thumbnail = images.get(0);
        }
    }

    public Product(String name, String description, float price, String category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
    }
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public void setImageFile(List<Image> images) {
        this.images = images;
    }

    public Image getThumbnail(){
        return this.thumbnail;
    }

    public void setThumbnail(Image thumbnail){
        this.thumbnail = thumbnail;
    }

    public boolean isSold() {
        return this.sold;
    }

    public void setSold(boolean sold) {
        this.sold = sold;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User user1) {
        this.owner = user1;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }

    @Override
    public String toString() {
        return "Product [id=" + id + ", name=" + name + ", description=" + description + "]";
    }
    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<User> getFavoriteOfUsers() {
        return favoriteOfUser;
    }

    public void setFavoriteOfUsers(List<User> favoriteOfUsers) {
        this.favoriteOfUser = favoriteOfUsers;
    }

    public List<Report> getReports() {
        return reports;
    }
    
    public void setReports(List<Report> reports) {
        this.reports = reports;
    }

    public boolean hasImage() {
        return hasImage;
    }

    public void setHasImage(boolean hasImage) {
        this.hasImage = hasImage;
    }
}
