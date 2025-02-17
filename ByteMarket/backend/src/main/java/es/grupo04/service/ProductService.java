package es.grupo04.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.grupo04.model.Product;
import es.grupo04.repository.ProductRepository;

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

	public void save(Product product) {
		repository.save(product);
	}

	public void delete(long id) {
		repository.deleteById(id);
	}
}

