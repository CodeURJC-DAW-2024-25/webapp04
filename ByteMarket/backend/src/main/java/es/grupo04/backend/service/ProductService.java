package es.grupo04.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.grupo04.backend.model.Product;
import es.grupo04.backend.repository.ProductRepository;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;

	public Optional<Product> findById(long id) {
		return repository.findById(id);
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
}

