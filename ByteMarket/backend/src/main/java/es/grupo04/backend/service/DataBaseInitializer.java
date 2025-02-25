package es.grupo04.backend.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.grupo04.backend.model.Product;
import es.grupo04.backend.model.User;
import es.grupo04.backend.model.Image;
import es.grupo04.backend.repository.ProductRepository;
import es.grupo04.backend.repository.UserRepository;
import es.grupo04.backend.repository.ImageRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import java.io.File;
import java.sql.Blob;
import java.sql.SQLException;

@Service
public class DataBaseInitializer {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageRepository ImageRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public DataBaseInitializer() {
    }

    @PostConstruct
    public void init() throws IOException, URISyntaxException, SerialException, SQLException {
        // Crear usuarios
        User user1 = new User("Alice", passwordEncoder.encode("password123"), "alice@example.com","USER");
        User user2 = new User("Bob", passwordEncoder.encode("securePass456"), "bob@example.com","ADMIN","USER");

        userRepository.save(user1);
        userRepository.save(user2);


        // Read the image file from the static/images directory
        Path imagePath = Paths.get("src", "main", "resources", "static", "images", "auriculares.jpg");
        File imageFile = imagePath.toFile();
        byte[] imageData = Files.readAllBytes(imageFile.toPath());

        // Create a Blob from the image data
        Blob imageBlob = new SerialBlob(imageData);
        Image image = new Image(imageBlob); 
        Image image2 = new Image(imageBlob);
        ArrayList<Image> images = new ArrayList<>();
        images.add(image);
        images.add(image2); //tienen que ser distintas
        

        ImageRepository.save(image);
        ImageRepository.save(image2);
   

        // Crear productos y asignarles propietarios
        List<Product> productsUser1 = List.of(
            new Product("Laptop", "Laptop potente para desarrollo", 1200, "Electrónica"),
            new Product("Smartphone", "Teléfono con gran autonomía", 800, "Electrónica"),
            new Product("Auriculares", "Auriculares inalámbricos con cancelación de ruido", 150, "Accesorios"),
            new Product("Monitor", "Monitor 4K UHD para edición de video", 600, "Electrónica"),
            new Product("Teclado Mecánico", "Teclado mecánico con switches rojos para gaming", 100, "Accesorios")
        );

        productsUser1.get(0).setImageFile(images);

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
