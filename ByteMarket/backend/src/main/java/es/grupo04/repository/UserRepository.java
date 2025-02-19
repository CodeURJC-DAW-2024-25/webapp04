package es.grupo04.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import es.grupo04.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByName(String name);

    User findByEmail(String email);

    User findByRole(String role);

}

