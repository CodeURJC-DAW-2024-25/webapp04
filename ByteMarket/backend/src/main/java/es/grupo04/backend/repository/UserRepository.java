package es.grupo04.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import es.grupo04.backend.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByName(String name);

    Optional<User> findByMail(String mail);

    Optional<User> findByRole(String role);

}

