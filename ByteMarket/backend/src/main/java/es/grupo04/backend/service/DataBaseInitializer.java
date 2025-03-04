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
        User deleteUser = new User ();
        deleteUser.setName("Usuario Eliminado");
        User user1 = new User("Pedro", passwordEncoder.encode("12341234"), "pedro@gmail.com","USER");
        User user2 = new User("Hugo", passwordEncoder.encode("securePass456"), "hugo@gmail.com","USER");
        User user3 = new User("Sara", passwordEncoder.encode("Password1234"), "sara@gmail.com","ADMIN");

        userRepository.save(deleteUser);
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        // AIR PODS
        // Read the image file from the static/images directory
        Path imagePath = Paths.get("src", "main", "resources", "static", "images", "auriculares1.jpg");
        File imageFile = imagePath.toFile();
        byte[] imageData = Files.readAllBytes(imageFile.toPath());

        Path imagePath2 = Paths.get("src", "main", "resources", "static", "images", "auriculares2.jpg");
        File imageFile2 = imagePath2.toFile();
        byte[] imageData2 = Files.readAllBytes(imageFile2.toPath());

        Path imagePath3 = Paths.get("src", "main", "resources", "static", "images", "auriculares3.jpg");
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
        
        // AURICULARES INALÁMBRICOS
        Path imagePath18 = Paths.get("src", "main", "resources", "static", "images", "auricularesinalambricos1.jpg");
        File imageFile18 = imagePath18.toFile();
        byte[] imageData18 = Files.readAllBytes(imageFile18.toPath());

        Path imagePath19 = Paths.get("src", "main", "resources", "static", "images", "auricularesinalambricos1.jpg");
        File imageFile19 = imagePath19.toFile();
        byte[] imageData19 = Files.readAllBytes(imageFile19.toPath());

        Blob imageBlob18 = new SerialBlob(imageData18);
        Image image18 = new Image(imageBlob18); 
        Blob imageBlob19 = new SerialBlob(imageData19);
        Image image19 = new Image(imageBlob19);
        ArrayList<Image> images8 = new ArrayList<>();
        images8.add(image18);
        images8.add(image19);

        // PC TORRE
        Path imagePath20 = Paths.get("src", "main", "resources", "static", "images", "pc1.jpg");
        File imageFile20 = imagePath20.toFile();
        byte[] imageData20 = Files.readAllBytes(imageFile20.toPath());

        Path imagePath21 = Paths.get("src", "main", "resources", "static", "images", "pc2.jpg");
        File imageFile21 = imagePath21.toFile();
        byte[] imageData21 = Files.readAllBytes(imageFile21.toPath());

        Blob imageBlob20 = new SerialBlob(imageData20);
        Image image20 = new Image(imageBlob20); 
        Blob imageBlob21 = new SerialBlob(imageData21);
        Image image21 = new Image(imageBlob21);
        ArrayList<Image> images9 = new ArrayList<>();
        images9.add(image20);
        images9.add(image21);
  
        // SMARTWATCH
        Path imagePath22 = Paths.get("src", "main", "resources", "static", "images", "reloj1.jpg");
        File imageFile22 = imagePath22.toFile();
        byte[] imageData22 = Files.readAllBytes(imageFile22.toPath());

        Path imagePath23 = Paths.get("src", "main", "resources", "static", "images", "reloj2.jpg");
        File imageFile23 = imagePath23.toFile();
        byte[] imageData23 = Files.readAllBytes(imageFile23.toPath());

        Blob imageBlob22 = new SerialBlob(imageData22);
        Image image22 = new Image(imageBlob22); 
        Blob imageBlob23 = new SerialBlob(imageData23);
        Image image23 = new Image(imageBlob23);
        ArrayList<Image> images10 = new ArrayList<>();
        images10.add(image22);
        images10.add(image23);

        // Create products in a list
        List<Product> productsUser1 = List.of(
            new Product("Air Pods Apple", "Auriculares inalámbricos de alta calidad con tecnología de cancelación de ruido activa. Incluyen un estuche de carga que proporciona hasta 24 horas de autonomía y conectividad Bluetooth de última generación para una experiencia de audio envolvente.", 590, "Auriculares", images),
            new Product("Portatil Toshiba", "Ordenador portátil diseñado para el rendimiento y la productividad. Equipado con un procesador Intel Core i5 de décima generación, 8GB de RAM y un disco SSD de 512GB, ideal para multitarea y aplicaciones exigentes.", 400, "Ordenadores", images3),
            new Product("Xiaomi Redmi Note 11", "Smartphone con pantalla AMOLED de 6.43 pulgadas y tasa de refresco de 90Hz. Cuenta con una cámara principal de 50 MP, batería de 5000mAh con carga rápida de 33W y procesador Snapdragon 680 para un rendimiento fluido.", 200, "Móviles", images2),
            new Product("Teclado Mecánico", "Teclado mecánico retroiluminado con switches personalizables para una experiencia de escritura y gaming precisa. Dispone de estructura ergonómica, teclas anti-ghosting y compatibilidad con Windows y macOS.", 80, "Otros", images6),
            new Product("Monitor HP", "Monitor de 27 pulgadas con resolución Full HD y tecnología antirreflejo. Ofrece colores vibrantes, amplio ángulo de visión y conectividad HDMI y DisplayPort, ideal para trabajo, entretenimiento y gaming.", 250, "Otros", images5)
        );


        List<Product> productsUser2 = List.of(
            new Product("Cámara Nikon", "Cámara profesional de alta resolución con sensor de 24 MP y sistema de enfoque avanzado. Ideal para fotografía y video en alta calidad, con conectividad WiFi y Bluetooth para compartir imágenes al instante.", 800, "Otros", images4),
            new Product("Tablet", "Tableta con pantalla de 10.1 pulgadas y resolución Full HD. Equipada con un potente procesador, 4GB de RAM y 64GB de almacenamiento, ideal para entretenimiento, trabajo y estudio.", 900, "Tablets", images7),
            new Product("Auriculares inalámbricos", "Auriculares Bluetooth con cancelación de ruido activa, sonido envolvente y batería de larga duración. Diseño ergonómico para un ajuste cómodo y micrófono integrado para llamadas claras.", 450, "Auriculares", images8),
            new Product("PC Gaming", "Amd Ryzen 5 3600 (6/12)\r\n" + //
                                "🔸P.Base Gigabyte A520M S2H\r\n" + //
                                "🔸16 GB DDR4 Kingston 3200 (2x8GB)\r\n" + //
                                "🔸Disco m2 nvme SDD Kingston 1TB.\r\n" + //
                                "🔸T.Gráfica Intel ARC A380 6GB GDDR6.\r\n" + //
                                "🔸Torre ATX Montech X3 Mesh Black.\r\n" + //
                                "🔸F.Alimentación Nox VX 650W Bronze.\r\n" + //
                                "🔸Windows 11 Pro.", 250, "Ordenadores", images9),
            new Product("Smartwatch", "Compatible con iPhone, hace/contesta llamadas, compatible con Android/iphone, aprueba de agua, bateria de larga duracion, pantalla tactil, control de música, registro de actividad fisica, diversas esferas de reloj, cable USB para su correspondiente carga, eleccion ideal para regalar.", 200, "SmartWatches", images10)
        );


        // Assign owner to each product
        productsUser1.forEach(p -> p.setOwner(user1));
        productsUser2.forEach(p -> p.setOwner(user2));

        // Save products in bd
        productRepository.saveAll(productsUser1);
        productRepository.saveAll(productsUser2);
    }
}
