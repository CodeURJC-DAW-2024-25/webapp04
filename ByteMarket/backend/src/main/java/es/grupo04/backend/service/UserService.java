package es.grupo04.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import es.grupo04.backend.model.User;
import es.grupo04.backend.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public User saveUser(User user) {
        String encodedPassword = passwordEncoder.encode(user.getEncodedPassword());
        user.setEncodedPassword(encodedPassword);
        return repository.save(user);
    }

    public Optional<User> findByMail(String mail) {
        return repository.findByMail(mail);
    }
}
