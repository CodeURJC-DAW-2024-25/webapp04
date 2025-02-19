package es.grupo04.model;

import java.sql.Blob;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;

@Entity
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id = null;
	
	private String title;
	private Float price;
	private String category;
	
	@Column(columnDefinition = "TEXT")
	private String description;

	@ManyToOne
	private User owner;

	@Lob
	private Blob imageFile; //TODO tiene que ser una lista, ElementCollection???
	private boolean image;

	/*
	@OneToOne
	private Purchase purchase; No parece necesario*/
	private boolean sold = false;


	public Product() {}

	public Product(String title, String description, Float price, String category) {
		this.title = title;
		this.description = description;
		this.price = price;
		this.category = category;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public boolean isSold() {
		return sold;
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

	@Override
	public String toString() {
		return "Book [id=" + id + ", title=" + title + ", description=" + description + "]";
	}
}
