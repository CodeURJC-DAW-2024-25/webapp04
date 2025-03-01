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
        // Create users
        User user1 = new User("Alice", passwordEncoder.encode("1234"), "a@example.com","USER");
        User user2 = new User("Bob", passwordEncoder.encode("securePass456"), "bob@example.com","USER");
        User user3 = new User("Jim", passwordEncoder.encode("Password1234"), "jim@example.com","ADMIN");

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);


        // Read the image file from the static/images directory
        Path imagePath = Paths.get("src", "main", "resources", "static", "images", "auriculares.jpg");
        File imageFile = imagePath.toFile();
        byte[] imageData = Files.readAllBytes(imageFile.toPath());

        // Create a Blob from the image data
        Blob imageBlob = new SerialBlob(imageData);
        Image image = new Image(imageBlob); 
        Image image2 = new Image(imageBlob);
        Image image3 = new Image(imageBlob);
        ArrayList<Image> images = new ArrayList<>();
        images.add(image);
        images.add(image2); //tienen que ser distintas
        images.add(image3);
        

        /*ImageRepository.save(image);
        ImageRepository.save(image2);*/
   

        // Create products in a list
        List<Product> productsUser1 = List.of(
            new Product("Auriculares", "Auriculares inalámbricos con cancelación de ruido", 150, "Auriculares", images),
            new Product("Laptop", "Laptop potente para desarrollo", 1200, "Ordenadores"),
            new Product("Smartphone", "Teléfono con gran autonomía", 800, "Móviles"),
            new Product("Monitor", "Monitor 4K UHD para edición de video", 600, "Otros"),
            new Product("Teclado Mecánico", "Teclado mecánico con switches rojos para gaming", 100, "Otros"),
            new Product("Aspiradora para torre", "Aspria todo el polvo dentro de tu torre", 100, "Otros"),
            new Product("Alfombrilla para ratón", "Alfombrilla con RGB cuadrada", 40, "Otros")
        );

        List<Product> productsUser2 = List.of(
            new Product("Cámara DSLR", "Cámara profesional para fotografía", 1000, "Otros"),
            new Product("Tablet", "Tablet de alta gama para diseño gráfico", 900, "Tablets"),
            new Product("Impresora 3D", "Impresora 3D de precisión para prototipado", 450, "Otros"),
            new Product("Silla ergonómica", "Silla de oficina cómoda y ajustable", 250, "Otros"),
            new Product("SmartWatch Xiaomi", "SmartWatch que calcula tus pasos", 450, "SmartWatches"),
            new Product("Ratón RGB", "Ratón con 12000 clicks", 90, "Otros"),
            new Product("Cable USB", "Cable USB para móvil", 15, "Otros"),
            new Product("Altavoz Sony", "Altavoz vintage con potencia sonora", 200, "Otros"),
            new Product("Cámara de seguridad", "Cámara de vigilancia WiFi", 150, "Otros"),
            new Product("Cámara Nikon", "Cámara de alta resolucion", 150, "Otros"),
            new Product("Mochila para portátil", "Mochila resistente para laptops", 50, "Otros")
        );


        // Assign owner to each product
        productsUser1.forEach(p -> p.setOwner(user1));
        productsUser2.forEach(p -> p.setOwner(user2));

        // Save products in bd
        productRepository.saveAll(productsUser1);
        productRepository.saveAll(productsUser2);
    }
}
