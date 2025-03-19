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

import es.grupo04.backend.dto.NewProductDTO;
import es.grupo04.backend.dto.NewProductMapper;
import es.grupo04.backend.dto.ProductDTO;
import es.grupo04.backend.dto.ProductMapper;
import es.grupo04.backend.dto.UserDTO;
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

	@Autowired
	private ProductMapper productMapper;

	@Autowired
	private NewProductMapper newProductMapper;

	@Autowired
	private ImageService imageService;

	public Optional<ProductDTO> findById(long id) {
		return Optional.of(productMapper.toDTO(repository.findById(id).get()));
	}

	public Page<ProductDTO> findProductsByOwner(UserDTO ownerDTO, int page, int pageSize) {
		User owner = userRepository.findById(ownerDTO.id()).get();
		Pageable pageable = PageRequest.of(page, pageSize);
    	return repository.findProductByOwner(owner, pageable).map(productMapper::toDTO);
	}

	public boolean exist(long id) {
		return repository.existsById(id);
	}

	public List<ProductDTO> findAll() {
		return productMapper.toDTOs(repository.findAll());
	}

	// Save a product
	public ProductDTO save(NewProductDTO product) throws IOException {
		Product newProduct = newProductMapper.toDomain(product);
		addImages(newProduct, product.imageUpload());
		return productMapper.toDTO(repository.save(newProduct));
	}

	
	//Delete favorites //TODO revisar esto
	public void deleteFavorites(List<Product> products){
		for (Product product : products) {
			List<User> users = repository.findUsersByFavoriteProduct(product);
			for (User user : users) {
				user.getFavoriteProducts().remove(product);
				userRepository.save(user);
			}
		}
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
						+ "Un saludo,\nByteMarket";
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
	public void sold(ProductDTO productDTO, UserDTO buyerDTO) {
		Product product = repository.findById(productDTO.id()).get();
		User buyer = userRepository.findById(buyerDTO.id()).get();
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
						+ "Un saludo,\nByteMarket";
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
	public List<ProductDTO> getLastPurchases(UserDTO userDTO) {
		User user = userRepository.findById(userDTO.id()).get();
		return productMapper.toDTOs(
				purchaseRepository.findByBuyerOrderByPurchaseDateDesc(user).stream()
				.limit(8)
				.map(Purchase::getProduct)
				.collect(Collectors.toList()));
	}

	// Get the last sales of a user
	public List<ProductDTO> getLastSales(UserDTO userDTO) {
		User user = userRepository.findById(userDTO.id()).get();
		return productMapper.toDTOs(
				purchaseRepository.findBySellerOrderByPurchaseDateDesc(user).stream()
				.limit(8)
				.map(Purchase::getProduct)
				.collect(Collectors.toList()));
	}

	// Get favorite products
	public Page<ProductDTO> getFavoriteProducts(UserDTO userDTO, int page, int size) {
		User user = userRepository.findById(userDTO.id()).get();
		Pageable pageable = PageRequest.of(page, size);
		return repository.findFavoriteProducts(user.getFavoriteProducts(), pageable).map(productMapper::toDTO);
	}

	// Add images to a product
	// Uses product because this method is called from the save method
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
	/* 
	public List<Product> findPaginated(int page, int pageSize) {
		Pageable pageable = PageRequest.of(page, pageSize);
		Page<Product> productPage = repository.findAll(pageable);
		return productPage.getContent();
	}*/

	// Pagination for available products
	public Page<ProductDTO> findPaginatedAvailable(int page, int pageSize) {
		Pageable pageable = PageRequest.of(page, pageSize);
		return repository.findAllByAvailableTrue(pageable).map(productMapper::toDTO);
	}

	// Pagination for available products in a specific category
	public Page<ProductDTO> findAvailableByCategory(String category, int page, int pageSize) {
		Pageable pageable = PageRequest.of(page, pageSize);
		return repository.findByCategoryAndAvailableTrue(category, pageable).map(productMapper::toDTO);
	}


	// To search by name
	public Page<ProductDTO> searchByName(String searchTerm, int page, int pageSize) {
		String normalizedSearchTerm = normalizeText(searchTerm);
		Pageable pageable = PageRequest.of(page, pageSize);
		return repository.findByNameContainingIgnoreCase(normalizedSearchTerm, pageable).map(productMapper::toDTO);
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

	public float calculateRating(UserDTO ownerDTO) {
		User owner = userRepository.findById(ownerDTO.id()).get();
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

	public List<ProductDTO> findTopRatedSellersProducts() {
        return productMapper.toDTOs(repository.findTopRatedSellersProducts());
    }

    public void addImageEditing(ProductDTO productDTO, MultipartFile image) throws IOException {
		Product product = repository.findById(productDTO.id()).get();		

		Blob blob = BlobProxy.generateProxy(image.getInputStream(), image.getSize());
		Image imgToStore = new Image(blob);

		product.getImages().add(imgToStore);
		repository.save(product);
    }

    public void updateProduct(ProductDTO oldProductDTO, NewProductDTO newProductDTO) {
		Product oldProduct = repository.findById(oldProductDTO.id()).get();
		Product product = newProductMapper.toDomain(newProductDTO);
		oldProduct.setName(product.getName());
		oldProduct.setDescription(product.getDescription());
		oldProduct.setCategory(product.getCategory());
		oldProduct.setPrice(product.getPrice());
		repository.save(oldProduct);
    }

	public void removeImage(long productId, long id) {
		Product product = repository.findById(productId).get();
		if(product.getThumbnail().getId() == id){
			product.setThumbnail(product.getImages().get(1));
		}
		product.getImages().remove(imageService.findById(id).get());
		imageService.delete(id);
	}		



}
