package es.grupo04.backend.service;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import es.grupo04.backend.model.Image;
import es.grupo04.backend.model.Product;
import es.grupo04.backend.model.User;
import es.grupo04.backend.repository.ImageRepository;
import es.grupo04.backend.repository.ProductRepository;
import es.grupo04.backend.repository.UserRepository;
import jakarta.annotation.PostConstruct;

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
        User user1 = new User("Alice", passwordEncoder.encode("1234"), "a@example.com","USER");
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
        

        /*ImageRepository.save(image);
        ImageRepository.save(image2);*/
   

        // Crear productos y asignarles propietarios
        List<Product> productsUser1 = List.of(
            new Product("Auriculares", "Auriculares inalámbricos con cancelación de ruido", 150, "Auriculares", images),
            new Product("Laptop", "Laptop potente para desarrollo", 1200, "Ordenadores"),
            new Product("Smartphone", "Teléfono con gran autonomía", 800, "Móviles"),
            new Product("Monitor", "Monitor 4K UHD para edición de video", 600, "Otros"),
            new Product("Teclado Mecánico", "Teclado mecánico con switches rojos para gaming", 100, "Otros")
        );

        List<Product> productsUser2 = List.of(
            new Product("Cámara DSLR", "Cámara profesional para fotografía", 1000, "otros"),
            new Product("Tablet", "Tablet de alta gama para diseño gráfico", 900, "Tablets"),
            new Product("Impresora 3D", "Impresora 3D de precisión para prototipado", 450, "Otros"),
            new Product("Silla ergonómica", "Silla de oficina cómoda y ajustable", 250, "Otros"),
            new Product("Reloj inteligente", "Smartwatch con múltiples funciones de salud", 200, "SmartWatches")
        );


        // Asignar propietario a cada producto
        productsUser1.forEach(p -> p.setOwner(user1));
        productsUser2.forEach(p -> p.setOwner(user2));

        // Guardar productos en la base de datos
        productRepository.saveAll(productsUser1);
        productRepository.saveAll(productsUser2);
    }
}
