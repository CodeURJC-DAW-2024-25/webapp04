package es.grupo04.service;

import java.io.IOException;
import java.net.URISyntaxException;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import es.grupo04.model.Product;
import es.grupo04.repository.ProductRepository;
import es.grupo04.repository.ShopRepository;
import es.grupo04.repository.UserRepository;
import jakarta.annotation.PostConstruct;

@Service
public class DataBaseInitializer {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ShopRepository shopRepository;

	@Autowired
	private UserRepository userRepository;


	@PostConstruct
	public void init() throws IOException, URISyntaxException {

		// Sample products
		Product product1 = new Product("PC gaming", "...");
		setProductImage(product1, "/public/defaul-pfp.jpg");
		productRepository.save(product1);

		Product product2 = new Product("Auriculares Sony", "...");
		setProductImage(product2, "/public/defaul-pfp.jpg");
		productRepository.save(product2);


		// Sample users


	}

	public void setProductImage(Product product, String classpathResource) throws IOException {
		product.setImage(true);
		Resource image = new ClassPathResource(classpathResource);
		product.setImageFile(BlobProxy.generateProxy(image.getInputStream(), image.contentLength()));
	}
}