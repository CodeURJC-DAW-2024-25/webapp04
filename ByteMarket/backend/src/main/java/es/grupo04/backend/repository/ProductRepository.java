package es.grupo04.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.grupo04.backend.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
