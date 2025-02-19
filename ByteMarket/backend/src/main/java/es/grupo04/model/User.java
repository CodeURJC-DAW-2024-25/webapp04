package es.grupo04.model;

import java.sql.Blob;
import java.util.List;

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

	private String role;
	private String name;
	private String encodedPassword;

	@Lob
	private Blob imageFile;
	private boolean image;

	@OneToMany(mappedBy = "owner", cascade=CascadeType.ALL, orphanRemoval=true)
	private List<Product> products;

	@OneToMany(mappedBy = "reviewedUser", cascade=CascadeType.ALL, orphanRemoval=true)
	private List<Review> reviews;
	@OneToMany(mappedBy = "reviewOwner", cascade=CascadeType.ALL, orphanRemoval=true)
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

	public User() {
	}


	public User(String name, String encodedPassword, String role, String mail) {
		this.name = name;
		this.encodedPassword = encodedPassword;
		this.role = role;
		this.mail = mail;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return encodedPassword;
	}

	public void setPassword(String password) {
		this.encodedPassword = password;
	}

	public String getRole() {
		return role;
	}

	public void setRoles(String role) {
		this.role = role;
	}

	public Blob getImageFile() {
		return imageFile;
	}

	public void setImageFile(Blob image) {
		this.imageFile = image;
	}

	public boolean getImage(){
		return this.image;
	}

	public void setImage(boolean image){
		this.image = image;
	}
}