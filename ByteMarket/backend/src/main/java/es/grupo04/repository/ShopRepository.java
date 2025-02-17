package es.grupo04.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.grupo04.model.Shop;

public interface ShopRepository extends JpaRepository<Shop, Long> {

}
