package es.grupo04.backend.service;

import java.io.IOException;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import es.grupo04.backend.model.Image;
import es.grupo04.backend.model.Product;
import es.grupo04.backend.model.Purchase;
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
            //Look for users with the product as a favorite
            List<User> usersWithFavoriteProduct = repository.findUsersByFavoriteProduct(product);
            //Eliminate the product from the favorites of the users
            for (User user : usersWithFavoriteProduct) {
                user.getFavoriteProducts().remove(product);	
                userRepository.save(user);
            }
            //Eliminate the product from the owner's products
            repository.deleteById(id);
        }
	}
	// Get the last purchases of a users
    public List<Purchase> getLastPurchases(User user) {
        return purchaseRepository.findByBuyerOrderByPurchaseDateDesc(user).stream()
                .limit(5)
                .collect(Collectors.toList());
    }
	// Get the last sales of a user
	public List<Purchase> getLastSales(User user) {
        return purchaseRepository.findBySellerOrderByPurchaseDateDesc(user);
    }
	// Add images to a product
	public void addImages(Product product, MultipartFile[] images) throws IOException {
		ArrayList<Image> imagesToStore = new ArrayList<>();
        for(MultipartFile image : images) {
			Blob blob = BlobProxy.generateProxy(image.getInputStream(),image.getSize());
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
	
	

}

