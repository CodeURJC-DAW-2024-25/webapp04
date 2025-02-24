package es.grupo04.backend.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import es.grupo04.backend.model.User;
import es.grupo04.backend.repository.UserRepository;

@Service
public class RepositoryUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {

        // Añadimos un log para ver si llega correctamente el email
        System.out.println("Buscando usuario con email: " + mail);

        User user = userRepository.findByMail(mail)
                .orElseThrow(() -> {
                    System.out.println("Usuario no encontrado con el email: " + mail); // Log si no se encuentra el usuario
                    return new UsernameNotFoundException("User not found");
                });

        // Mostramos el usuario encontrado (si lo encuentra)
        System.out.println("Usuario encontrado: " + user.getName());

        List<GrantedAuthority> roles = new ArrayList<>();
        for (String role : user.getRoles()) {
            System.out.println("Rol encontrado: " + role); // Log para ver los roles
            roles.add(new SimpleGrantedAuthority("ROLE_" + role));
        }

        // Mostramos la contraseña codificada del usuario
        System.out.println("Contraseña codificada del usuario: " + user.getEncodedPassword());

        return new org.springframework.security.core.userdetails.User(user.getName(), 
                user.getEncodedPassword(), roles);
    }
}
