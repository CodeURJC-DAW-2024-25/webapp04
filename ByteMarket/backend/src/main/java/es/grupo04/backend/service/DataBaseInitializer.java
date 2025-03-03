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
        User user1 = new User("Pedro", passwordEncoder.encode("1234"), "pedro@gmail.com","USER");
        User user2 = new User("Hugo", passwordEncoder.encode("securePass456"), "hugo@gmail.com","USER");
        User user3 = new User("Sara", passwordEncoder.encode("Password1234"), "sara@gmail.com","ADMIN");

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        // AIR PODS
        // Read the image file from the static/images directory
        Path imagePath = Paths.get("src", "main", "resources", "static", "images", "auriculares1.jpeg");
        File imageFile = imagePath.toFile();
        byte[] imageData = Files.readAllBytes(imageFile.toPath());

        Path imagePath2 = Paths.get("src", "main", "resources", "static", "images", "auriculares2.jpeg");
        File imageFile2 = imagePath2.toFile();
        byte[] imageData2 = Files.readAllBytes(imageFile2.toPath());

        Path imagePath3 = Paths.get("src", "main", "resources", "static", "images", "auriculares3.jpeg");
        File imageFile3 = imagePath3.toFile();
        byte[] imageData3 = Files.readAllBytes(imageFile3.toPath());

        // Create a Blob from the image data
        Blob imageBlob = new SerialBlob(imageData);
        Image image = new Image(imageBlob); 
        Blob imageBlob2 = new SerialBlob(imageData2);
        Image image2 = new Image(imageBlob2);
        Blob imageBlob3 = new SerialBlob(imageData3);
        Image image3 = new Image(imageBlob3);
        ArrayList<Image> images = new ArrayList<>();
        images.add(image);
        images.add(image2); 
        images.add(image3);

        // XIAOMI
        Path imagePath4 = Paths.get("src", "main", "resources", "static", "images", "xiaomi1.jpg");
        File imageFile4 = imagePath4.toFile();
        byte[] imageData4 = Files.readAllBytes(imageFile4.toPath());

        Path imagePath5 = Paths.get("src", "main", "resources", "static", "images", "xiaomi2.jpg");
        File imageFile5 = imagePath5.toFile();
        byte[] imageData5 = Files.readAllBytes(imageFile5.toPath());

        Path imagePath6 = Paths.get("src", "main", "resources", "static", "images", "xiaomi3.jpg");
        File imageFile6 = imagePath6.toFile();
        byte[] imageData6 = Files.readAllBytes(imageFile6.toPath());

        Blob imageBlob4 = new SerialBlob(imageData4);
        Image image4 = new Image(imageBlob4); 
        Blob imageBlob5 = new SerialBlob(imageData5);
        Image image5 = new Image(imageBlob5);
        Blob imageBlob6 = new SerialBlob(imageData6);
        Image image6 = new Image(imageBlob6);
        ArrayList<Image> images2 = new ArrayList<>();
        images2.add(image4);
        images2.add(image5); 
        images2.add(image6);

        // TOSHIBA
        Path imagePath7 = Paths.get("src", "main", "resources", "static", "images", "toshiba1.jpg");
        File imageFile7 = imagePath7.toFile();
        byte[] imageData7 = Files.readAllBytes(imageFile7.toPath());

        Path imagePath8 = Paths.get("src", "main", "resources", "static", "images", "toshiba2.jpg");
        File imageFile8 = imagePath8.toFile();
        byte[] imageData8 = Files.readAllBytes(imageFile8.toPath());

        Path imagePath9 = Paths.get("src", "main", "resources", "static", "images", "toshiba3.jpg");
        File imageFile9 = imagePath9.toFile();
        byte[] imageData9 = Files.readAllBytes(imageFile9.toPath());

        Blob imageBlob7 = new SerialBlob(imageData7);
        Image image7 = new Image(imageBlob7); 
        Blob imageBlob8 = new SerialBlob(imageData8);
        Image image8 = new Image(imageBlob8);
        Blob imageBlob9 = new SerialBlob(imageData9);
        Image image9 = new Image(imageBlob9);
        ArrayList<Image> images3 = new ArrayList<>();
        images3.add(image7);
        images3.add(image8); 
        images3.add(image9);

        // CAMARA
        Path imagePath10 = Paths.get("src", "main", "resources", "static", "images", "camara1.jpg");
        File imageFile10 = imagePath10.toFile();
        byte[] imageData10 = Files.readAllBytes(imageFile10.toPath());

        Path imagePath11 = Paths.get("src", "main", "resources", "static", "images", "camara2.jpg");
        File imageFile11 = imagePath11.toFile();
        byte[] imageData11 = Files.readAllBytes(imageFile11.toPath());

        Blob imageBlob10 = new SerialBlob(imageData10);
        Image image10 = new Image(imageBlob10); 
        Blob imageBlob11 = new SerialBlob(imageData11);
        Image image11 = new Image(imageBlob11);
        ArrayList<Image> images4 = new ArrayList<>();
        images4.add(image10);
        images4.add(image11);

        // MONITOR
        Path imagePath12 = Paths.get("src", "main", "resources", "static", "images", "monitor1.jpg");
        File imageFile12 = imagePath12.toFile();
        byte[] imageData12 = Files.readAllBytes(imageFile12.toPath());

        Path imagePath13 = Paths.get("src", "main", "resources", "static", "images", "monitor2.jpg");
        File imageFile13 = imagePath13.toFile();
        byte[] imageData13 = Files.readAllBytes(imageFile13.toPath());

        Blob imageBlob12 = new SerialBlob(imageData12);
        Image image12 = new Image(imageBlob12); 
        Blob imageBlob13 = new SerialBlob(imageData13);
        Image image13 = new Image(imageBlob13);
        ArrayList<Image> images5 = new ArrayList<>();
        images5.add(image12);
        images5.add(image13);

        // TECLADO
        Path imagePath14 = Paths.get("src", "main", "resources", "static", "images", "teclado1.jpg");
        File imageFile14 = imagePath14.toFile();
        byte[] imageData14 = Files.readAllBytes(imageFile14.toPath());

        Path imagePath15 = Paths.get("src", "main", "resources", "static", "images", "teclado2.jpg");
        File imageFile15 = imagePath15.toFile();
        byte[] imageData15 = Files.readAllBytes(imageFile15.toPath());

        Blob imageBlob14 = new SerialBlob(imageData14);
        Image image14 = new Image(imageBlob14); 
        Blob imageBlob15 = new SerialBlob(imageData15);
        Image image15 = new Image(imageBlob15);
        ArrayList<Image> images6 = new ArrayList<>();
        images6.add(image14);
        images6.add(image15);

        // TABLET
        Path imagePath16 = Paths.get("src", "main", "resources", "static", "images", "tablet1.jpg");
        File imageFile16 = imagePath16.toFile();
        byte[] imageData16 = Files.readAllBytes(imageFile16.toPath());

        Path imagePath17 = Paths.get("src", "main", "resources", "static", "images", "tablet2.jpg");
        File imageFile17 = imagePath17.toFile();
        byte[] imageData17 = Files.readAllBytes(imageFile17.toPath());

        Blob imageBlob16 = new SerialBlob(imageData16);
        Image image16 = new Image(imageBlob16); 
        Blob imageBlob17 = new SerialBlob(imageData17);
        Image image17 = new Image(imageBlob17);
        ArrayList<Image> images7 = new ArrayList<>();
        images7.add(image16);
        images7.add(image17);
        

        /*ImageRepository.save(image);
        ImageRepository.save(image2);*/
   

        // Create products in a list
        List<Product> productsUser1 = List.of(
            new Product("Air Pods Apple", "", 590, "Auriculares", images),
            new Product("Portatil Toshiba", "", 400, "Ordenadores", images3),
            new Product("Xiaomi Redmi Note 11", "", 200, "Móviles", images2),
            new Product("Teclado Mecánico", "", 100, "Otros", images6),
            new Product("Monitor HP", "", 600, "Otros", images5)
        );

        List<Product> productsUser2 = List.of(
            new Product("Cámara Nikon", "",800, "Otros", images4),
            new Product("Tablet", "", 900, "Tablets", images7),
            new Product("Impresora 3D", "", 450, "Otros"),
            new Product("Silla ergonómica", "", 250, "Otros"),
            new Product("Reloj inteligente", "", 200, "SmartWatches")
        );


        // Assign owner to each product
        productsUser1.forEach(p -> p.setOwner(user1));
        productsUser2.forEach(p -> p.setOwner(user2));

        // Save products in bd
        productRepository.saveAll(productsUser1);
        productRepository.saveAll(productsUser2);
    }
}
