package es.grupo04.backend.model;

import java.sql.Blob;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id = null;
	
	private String name;
	private Float price;
	private String category;
	
	@Column(columnDefinition = "TEXT")
	private String description;

	@ManyToOne
	private User owner;

	@Lob
	private Blob imageFile; //TODO tiene que ser una lista, ElementCollection???
	private boolean image;

	// Añadir el campo 'sold'
	private boolean sold;

	@OneToMany(mappedBy = "product")
	private List<Purchase> purchases;

	public Product() {
    }

	public Product(String name, String description, float price, String category) {
		this.name = name;
		this.description = description;
		this.price = price;
		this.category = category;
	}

	public String getname() {
		return name;
	}

	public void setname(String name) {
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

	// Método isSold() ya funciona correctamente con 'sold'
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

	public void setOwner(User user1) {
        this.owner = user1;
    }

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", description=" + description + "]";
	}
}
