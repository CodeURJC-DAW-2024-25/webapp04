package es.grupo04.backend.service;

import java.io.IOException;
import java.sql.Blob;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.grupo04.backend.model.Image;
import es.grupo04.backend.model.Product;
import es.grupo04.backend.model.Purchase;
import es.grupo04.backend.model.Review;
import es.grupo04.backend.model.User;
import es.grupo04.backend.repository.ProductRepository;
import es.grupo04.backend.repository.PurchaseRepository;
import es.grupo04.backend.repository.UserRepository;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PurchaseRepository purchaseRepository;

	@Autowired
	private EmailService emailService;

	public Optional<Product> findById(long id) {
		return repository.findById(id);
	}

	public List<Product> findByOwner(User owner) {
		return repository.findByOwner(owner);
	}

	public boolean exist(long id) {
		return repository.existsById(id);
	}

	public List<Product> findAll() {
		return repository.findAll();
	}

	// Save a product
	public Product save(Product product) {
		return repository.save(product);
	}

	// Delete a product
	public void delete(long id) {
		Optional<Product> productOpt = repository.findById(id);
		if (productOpt.isPresent()) {
			Product product = productOpt.get();
			// Look for users with the product as a favorite
			List<User> usersWithFavoriteProduct = repository.findUsersByFavoriteProduct(product);
			// Send an email
			String name = product.getName();
			for (User user : usersWithFavoriteProduct) {

				String subject = name + " se ha ido";
				String body = "Hola " + user.getName() + ",\n\n"
						+ "Queríamos informarle que uno de sus productos (" + name
						+ ") favoritos ya no está disponible.\n"
						+ "Esperamos que encuentre alternativas de su interés en nuestra página\n\n"
						+ "Un saludo,\n ByteMarket";
				emailService.sendEmail(user.getMail(), subject, body);

			}
			// Eliminate the product from the favorites of the users
			for (User user : usersWithFavoriteProduct) {
				user.getFavoriteProducts().remove(product);
				userRepository.save(user);
			}
			// Eliminate the product from the owner's products
			repository.deleteById(id);
		}
	}

	// Sold a favorite product
	public void sold(Product product, User buyer) {
		// Look for users with the product as a favorite
		List<User> usersWithFavoriteProduct = repository.findUsersByFavoriteProduct(product);
		// Send an email
		String name = product.getName();
		for (User user : usersWithFavoriteProduct) {
			if (!user.equals(buyer)) {
				String subject = name + " ha sido vendido";
				String body = "Hola " + user.getName() + ",\n\n"
						+ "Queríamos informarle que uno de sus productos (" + name
						+ ") favoritos ha sido vendido.\n"
						+ "Esperamos que encuentre alternativas de su interés en nuestra página\n\n"
						+ "Un saludo,\n ByteMarket";
				emailService.sendEmail(user.getMail(), subject, body);
			}

		}
		// Eliminate the product from the favorites of the users
		for (User user : usersWithFavoriteProduct) {
			user.getFavoriteProducts().remove(product);
			userRepository.save(user);
		}
	}

	// Get the last purchases of a users
	public List<Product> getLastPurchases(User user) {
		return purchaseRepository.findByBuyerOrderByPurchaseDateDesc(user).stream()
				.limit(5)
				.map(Purchase::getProduct)
				.collect(Collectors.toList());
	}

	// Get the last sales of a user
	public List<Product> getLastSales(User user) {
		return purchaseRepository.findBySellerOrderByPurchaseDateDesc(user).stream()
				.limit(5)
				.map(Purchase::getProduct)
				.collect(Collectors.toList());
	}

	// Add images to a product
	public void addImages(Product product, MultipartFile[] images) throws IOException {
		ArrayList<Image> imagesToStore = new ArrayList<>();
		for (MultipartFile image : images) {
			Blob blob = BlobProxy.generateProxy(image.getInputStream(), image.getSize());
			Image imgToStore = new Image(blob);
			imagesToStore.add(imgToStore);
		}

		product.setImages(imagesToStore);
		product.setThumbnail(imagesToStore.get(0));
	}

	// Pagination
	public List<Product> findPaginated(int page, int pageSize) {
		Pageable pageable = PageRequest.of(page, pageSize);
		Page<Product> productPage = repository.findAll(pageable);
		return productPage.getContent();
	}

	// Pagination for available products
	public Page<Product> findPaginatedAvailable(int page, int pageSize) {
		Pageable pageable = PageRequest.of(page, pageSize);
		return repository.findAllByAvailableTrue(pageable);
	}

	// Pagination for available products in a specific category
	public Page<Product> findAvailableByCategory(String category, int page, int pageSize) {
		Pageable pageable = PageRequest.of(page, pageSize);
		return repository.findByCategoryAndAvailableTrue(category, pageable);
	}


	// To search by name
	public Page<Product> searchByName(String searchTerm, int page, int pageSize) {
		String normalizedSearchTerm = normalizeText(searchTerm);
		Pageable pageable = PageRequest.of(page, pageSize);
		return repository.findByNameContainingIgnoreCase(normalizedSearchTerm, pageable);
	}

	// To be able to search by names with accent ("Cámara")
	public static String normalizeText(String input) {
		if (input == null) {
			return null;
		}

		String normalized = input.toLowerCase();

		normalized = Normalizer.normalize(normalized, Normalizer.Form.NFD);
		normalized = normalized.replaceAll("\\p{M}", "");

		normalized = normalized.trim().replaceAll("\\s+", " ");

		return normalized;
	}

	public float calculateRating(User owner) {
		List<Review> reviews = owner.getReviews();
		if (reviews == null || reviews.isEmpty()) {
			return 0;
		}

		float total = 0;
		for (Review review : reviews) {
			review.getRating();
			total += review.getRating();
		}

		float average = total / reviews.size();
		average = Math.round(average * 100) / 100f;

		return average;
	}

	public List<Product> findTopRatedSellersProducts() {
        return repository.findTopRatedSellersProducts();
    }
	
	
	public Optional<User> findUserByName(String name) {
		return userRepository.findByName(name);
	}

    public void addImageEditing(Product product, MultipartFile image) throws IOException {
		
		Blob blob = BlobProxy.generateProxy(image.getInputStream(), image.getSize());
		Image imgToStore = new Image(blob);

		product.getImages().add(imgToStore);
    }

    public void updateProduct(Product oldProduct, Product product) {
		oldProduct.setName(product.getName());
		oldProduct.setDescription(product.getDescription());
		oldProduct.setCategory(product.getCategory());
		oldProduct.setPrice(product.getPrice());
    }		



}
