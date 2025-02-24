package es.grupo04.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import es.grupo04.backend.model.User;
import es.grupo04.backend.repository.UserRepository;

public class UserService {
    @Autowired
	private UserRepository repository;

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public User saveUser(User user) {
        return repository.save(user);
    }

    public Optional<User> findByMail(String mail) {
        return repository.findByMail(mail);
    }

}