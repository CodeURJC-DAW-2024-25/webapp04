package es.grupo04.backend.service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

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
        if (user.getRoles() == null) {
            user.setRoles(List.of("USER")); // Asignar rol por defecto si no se pasa en el formulario
        }
        String encodedPassword = passwordEncoder.encode(user.getEncodedPassword());
        user.setEncodedPassword(encodedPassword);
        return repository.save(user);
    }

    public Optional<User> findByMail(String mail) {
        return repository.findByMail(mail);
    }

    public Optional<User> findByName(String name) {
        return repository.findByName(name);
    }

    public boolean createAccount(User user){
        if (repository.findByMail(user.getMail()).isPresent()){
            return true;    //Si tiene una cuenta devolvemos true
        }else {
            saveUser(user);
            return false;
        }
    }
    //User Validation
    public boolean validateUser(User user, String confirmPassword) {
        if (user.getName() == null || user.getName().isEmpty()) {   //Check if name is empty
            return false;
        } else if (user.getName().length() > 16) {  //Check if name is longer than 16 characters
            return false;
        }
        String emailRegex = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";       //Email regex
        if (user.getMail() == null || user.getMail().isEmpty()) {   //Check if email is empty
            return false;
        } else if (!Pattern.matches(emailRegex, user.getMail())) {  //Check if email is valid
            return false;
        }

        if ((user.getEncodedPassword() == null || user.getEncodedPassword().isEmpty())||(confirmPassword == null || confirmPassword.isEmpty())) { //Check if password is empty
            return false;
        } else if (!user.getEncodedPassword().equals(confirmPassword)){ //Check if passwords match
            return false;
        }
        return true; // No errors
    }
}
