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

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;

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

	public Product save(Product product) {
		return repository.save(product);
	}

	public void delete(long id) {
		repository.deleteById(id);
	}

    public List<Purchase> getLastPurchases(User user) {
        return purchaseRepository.findByBuyerOrderByPurchaseDateDesc(user).stream()
                .limit(5)
                .collect(Collectors.toList());
    }

	public List<Purchase> getLastSales(User user) {
        return purchaseRepository.findBySellerOrderByPurchaseDateDesc(user);
    }

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

	public List<Product> findPaginated(int page, int pageSize) {
		Pageable pageable = PageRequest.of(page, pageSize);
		Page<Product> productPage = repository.findAll(pageable);
		return productPage.getContent();
	}
}

