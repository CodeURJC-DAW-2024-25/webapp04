package es.grupo04.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import es.grupo04.model.User;
import es.grupo04.repository.UserRepository;

public class UserService {
    @Autowired
	private UserRepository repository;

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public User saveUser(User user) {
        return repository.save(user);
    }

    public User findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public User getAdmin() {
        return repository.findByRole("ADMIN");
    }

}
