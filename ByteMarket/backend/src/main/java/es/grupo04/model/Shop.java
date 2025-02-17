package es.grupo04.model;

import java.util.List;
//import jakarta.persistence.*;

//@Entity
public class Shop {

	//@Id
	//@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id = null;
	
	private String name;
	
	//@Column(columnDefinition = "TEXT") // Esto sería para saber en qué tienda lo compró
	private String address;

	//@ManyToMany(mappedBy="shops")
 	private List<Product> products;


	public Shop() {}

	public Shop(String name, String address) {
		this.name = name;
		this.address = address;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}


	public List<Product> getProducts() {
		return products;
	}


	public void setProducts(List<Product> products) {
		this.products = products;
	}
}
