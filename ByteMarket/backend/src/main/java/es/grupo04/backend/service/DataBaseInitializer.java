package es.grupo04.backend.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.grupo04.backend.model.Product;
import es.grupo04.backend.model.User;
import es.grupo04.backend.repository.ProductRepository;
import es.grupo04.backend.repository.UserRepository;
import jakarta.annotation.PostConstruct;

@Service
public class DataBaseInitializer {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void init() throws IOException, URISyntaxException {
        // Crear usuarios
        User user1 = new User("Alice", "password123", "USER", "alice@example.com");
        User user2 = new User("Bob", "securePass456", "USER", "bob@example.com");

        userRepository.saveAll(List.of(user1, user2));

        // Crear productos y asignarles propietarios
        List<Product> productsUser1 = List.of(
        new Product("Laptop", "Laptop potente para desarrollo", 1200, "Electrónica"),
        new Product("Smartphone", "Teléfono con gran autonomía", 800, "Electrónica"),
        new Product("Auriculares", "Auriculares inalámbricos con cancelación de ruido", 150, "Accesorios"),
        new Product("Monitor", "Monitor 4K UHD para edición de video", 600, "Electrónica"),
        new Product("Teclado Mecánico", "Teclado mecánico con switches rojos para gaming", 100, "Accesorios")
    );

    List<Product> productsUser2 = List.of(
        new Product("Cámara DSLR", "Cámara profesional para fotografía", 1000, "Fotografía"),
        new Product("Tablet", "Tablet de alta gama para diseño gráfico", 900, "Electrónica"),
        new Product("Impresora 3D", "Impresora 3D de precisión para prototipado", 450, "Tecnología"),
        new Product("Silla ergonómica", "Silla de oficina cómoda y ajustable", 250, "Mobiliario"),
        new Product("Reloj inteligente", "Smartwatch con múltiples funciones de salud", 200, "Electrónica")
    );


        // Asignar propietario a cada producto
        productsUser1.forEach(p -> p.setOwner(user1));
        productsUser2.forEach(p -> p.setOwner(user2));

        // Guardar productos en la base de datos
        productRepository.saveAll(productsUser1);
        productRepository.saveAll(productsUser2);
    }
}
