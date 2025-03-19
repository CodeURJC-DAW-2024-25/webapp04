package es.grupo04.backend.service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.serial.SerialException;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;


import es.grupo04.backend.model.Chat;
import es.grupo04.backend.model.Image;
import es.grupo04.backend.model.Message;
import es.grupo04.backend.model.Product;
import es.grupo04.backend.model.Purchase;
import es.grupo04.backend.model.Review;
import es.grupo04.backend.model.User;
import es.grupo04.backend.repository.ChatRepository;
import es.grupo04.backend.repository.MessageRepository;
import es.grupo04.backend.repository.ProductRepository;
import es.grupo04.backend.repository.PurchaseRepository;
import es.grupo04.backend.repository.ReviewRepository;
import es.grupo04.backend.repository.UserRepository;
import jakarta.annotation.PostConstruct;

@Service
public class DataBaseInitializer {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    public DataBaseInitializer() {
    }

    public Image initImage(String classpathResource) throws IOException {
		Resource image = new ClassPathResource(classpathResource);
        InputStream inputStream = new BufferedInputStream(image.getInputStream());
        Image img = new Image(BlobProxy.generateProxy(inputStream, image.contentLength()));
        return img;
	}

    @PostConstruct
    public void init() throws IOException, URISyntaxException, SerialException, SQLException {
        // Create users
        User deleteUser = new User ();
        deleteUser.setName("Usuario Eliminado");
        User user1 = new User("Pedro", passwordEncoder.encode("12341234"), "pedro@gmail.com","USER");
        User user2 = new User("Hugo", passwordEncoder.encode("securePass456"), "hugo@gmail.com","USER");
        User user4 = new User("Alex", passwordEncoder.encode("securePass1234"), "alex@gmail.com","USER");
        User user3 = new User("Sara", passwordEncoder.encode("Password1234"), "sara@gmail.com","ADMIN");

        userRepository.save(deleteUser);
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);

        // AIR PODS
        // Create a Blob from the image data
        // AIR PODS
        Image image = initImage("/static/images/auriculares1.jpg"); 
        Image image2 = initImage("/static/images/auriculares2.jpg");
        Image image3 = initImage("/static/images/auriculares3.jpg");
        ArrayList<Image> images = new ArrayList<>(List.of(image, image2, image3));

        // XIAOMI
        Image image4 = initImage("/static/images/xiaomi1.jpg"); 
        Image image5 = initImage("/static/images/xiaomi2.jpg");
        Image image6 = initImage("/static/images/xiaomi3.jpg");
        ArrayList<Image> images2 = new ArrayList<>(List.of(image4, image5, image6));

        // TOSHIBA
        Image image7 = initImage("/static/images/toshiba1.jpg"); 
        Image image8 = initImage("/static/images/toshiba2.jpg");
        Image image9 = initImage("/static/images/toshiba3.jpg");
        ArrayList<Image> images3 = new ArrayList<>(List.of(image7, image8, image9));

        // CAMERA
        Image image10 = initImage("/static/images/camara1.jpg"); 
        Image image11 = initImage("/static/images/camara2.jpg");
        ArrayList<Image> images4 = new ArrayList<>(List.of(image10, image11));

        // MONITOR
        Image image12 = initImage("/static/images/monitor1.jpg"); 
        Image image13 = initImage("/static/images/monitor2.jpg");
        ArrayList<Image> images5 = new ArrayList<>(List.of(image12, image13));

        // KEYBOARD
        Image image14 = initImage("/static/images/teclado1.jpg"); 
        Image image15 = initImage("/static/images/teclado2.jpg");
        ArrayList<Image> images6 = new ArrayList<>(List.of(image14, image15));

        // TABLET
        Image image16 = initImage("/static/images/tablet1.jpg"); 
        Image image17 = initImage("/static/images/tablet2.jpg");
        ArrayList<Image> images7 = new ArrayList<>(List.of(image16, image17));

        // EARPHONES
        Image image18 = initImage("/static/images/auricularesinalambricos1.jpg"); 
        Image image19 = initImage("/static/images/auricularesinalambricos2.jpg");
        ArrayList<Image> images8 = new ArrayList<>(List.of(image18, image19));

        // PC 
        Image image20 = initImage("/static/images/pc1.jpg"); 
        Image image21 = initImage("/static/images/pc2.jpg");
        ArrayList<Image> images9 = new ArrayList<>(List.of(image20, image21));

        // SMARTWATCH
        Image image22 = initImage("/static/images/reloj1.jpg"); 
        Image image23 = initImage("/static/images/reloj2.jpg");
        ArrayList<Image> images10 = new ArrayList<>(List.of(image22, image23));

        // PRODUCTS 11-32
        Image image24 = initImage("/static/images/tecladoRazer.png"); 
        Image image25 = initImage("/static/images/ratonLogitech1.png");
        Image image26 = initImage("/static/images/lenovo1.png");
        Image image27 = initImage("/static/images/ssd1.png");
        Image image28 = initImage("/static/images/monitor.png");
        Image image29 = initImage("/static/images/iphoneProMax.png");
        Image image30 = initImage("/static/images/samsung24Ultra.png");
        Image image31 = initImage("/static/images/googlePixelPro.png");
        Image image32 = initImage("/static/images/xiaomi13Ultra.png");
        Image image33 = initImage("/static/images/onePlus.png");
        Image image34 = initImage("/static/images/sonyXPeria.png");
        Image image35 = initImage("/static/images/nothingPhone.png");
        Image image36 = initImage("/static/images/realmeGT.png");
        Image image37 = initImage("/static/images/dellXP.png");
        Image image38 = initImage("/static/images/macbookPro.png");
        Image image39 = initImage("/static/images/asusRog.png");
        Image image40 = initImage("/static/images/hpSpectre.png");
        Image image41 = initImage("/static/images/msiCreator.png");
        Image image42 = initImage("/static/images/appleWatch.png");
        Image image43 = initImage("/static/images/galaxyWatch.png");
        Image image44 = initImage("/static/images/garmingFenix.png");
        Image image45 = initImage("/static/images/googlePixelWatch.png");

        ArrayList<Image> images11 = new ArrayList<>(List.of(image24));
        ArrayList<Image> images12 = new ArrayList<>(List.of(image25));
        ArrayList<Image> images13 = new ArrayList<>(List.of(image26));
        ArrayList<Image> images14 = new ArrayList<>(List.of(image27));
        ArrayList<Image> images15 = new ArrayList<>(List.of(image28));
        ArrayList<Image> images16 = new ArrayList<>(List.of(image29));
        ArrayList<Image> images17 = new ArrayList<>(List.of(image30));
        ArrayList<Image> images18 = new ArrayList<>(List.of(image31));
        ArrayList<Image> images19 = new ArrayList<>(List.of(image32));
        ArrayList<Image> images20 = new ArrayList<>(List.of(image33));
        ArrayList<Image> images21 = new ArrayList<>(List.of(image34));
        ArrayList<Image> images22 = new ArrayList<>(List.of(image35));
        ArrayList<Image> images23 = new ArrayList<>(List.of(image36));
        ArrayList<Image> images24 = new ArrayList<>(List.of(image37));
        ArrayList<Image> images25 = new ArrayList<>(List.of(image38));
        ArrayList<Image> images26 = new ArrayList<>(List.of(image39));
        ArrayList<Image> images27 = new ArrayList<>(List.of(image40));
        ArrayList<Image> images28 = new ArrayList<>(List.of(image41));
        ArrayList<Image> images29 = new ArrayList<>(List.of(image42));
        ArrayList<Image> images30 = new ArrayList<>(List.of(image43));
        ArrayList<Image> images31 = new ArrayList<>(List.of(image44));
        ArrayList<Image> images32 = new ArrayList<>(List.of(image45));


        // Create products in a list
        List<Product> productsUser1 = List.of(
            new Product("Air Pods Apple", "Auriculares inalámbricos de alta calidad con tecnología de cancelación de ruido activa. Incluyen un estuche de carga que proporciona hasta 24 horas de autonomía y conectividad Bluetooth de última generación para una experiencia de audio envolvente.", 590, "Auriculares", images),
            new Product("Portatil Toshiba", "Ordenador portátil diseñado para el rendimiento y la productividad. Equipado con un procesador Intel Core i5 de décima generación, 8GB de RAM y un disco SSD de 512GB, ideal para multitarea y aplicaciones exigentes.", 400, "Ordenadores", images3),
            new Product("Xiaomi Redmi Note 11", "Smartphone con pantalla AMOLED de 6.43 pulgadas y tasa de refresco de 90Hz. Cuenta con una cámara principal de 50 MP, batería de 5000mAh con carga rápida de 33W y procesador Snapdragon 680 para un rendimiento fluido.", 200, "Móviles", images2),
            new Product("Teclado Mecánico", "Teclado mecánico retroiluminado con switches personalizables para una experiencia de escritura y gaming precisa. Dispone de estructura ergonómica, teclas anti-ghosting y compatibilidad con Windows y macOS.", 80, "Otros", images6),
            new Product("Monitor HP", "Monitor de 27 pulgadas con resolución Full HD y tecnología antirreflejo. Ofrece colores vibrantes, amplio ángulo de visión y conectividad HDMI y DisplayPort, ideal para trabajo, entretenimiento y gaming.", 250, "Otros", images5)
        );


        Product p1 = new Product("Cámara Nikon", "Cámara profesional de alta resolución con sensor de 24 MP y sistema de enfoque avanzado. Ideal para fotografía y video en alta calidad, con conectividad WiFi y Bluetooth para compartir imágenes al instante.", 800, "Otros", images4);
        List<Product> productsUser2 = List.of(
            p1,
            new Product("Tablet", "Tableta con pantalla de 10.1 pulgadas y resolución Full HD. Equipada con un potente procesador, 4GB de RAM y 64GB de almacenamiento, ideal para entretenimiento, trabajo y estudio.", 900, "Otros", images7),
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

        Product p2 = new Product("HP Spectre x360 14", "Convertible premium con pantalla OLED de 14, Intel Core i7, 16GB RAM y SSD de 1TB.", 1800, "Ordenadores", images27);
        Product p3 = new Product("Sony Xperia 1 V", "Pantalla 4K HDR OLED de 6.5, Snapdragon 8 Gen 2, triple cámara con sensor Exmor T y audio Hi-Res.", 1200, "Móviles", images21);
        Product p4 = new Product("Garmin Fenix 7X Pro", "Smartwatch multideporte con GPS de precisión, mapas topográficos, monitorización avanzada de salud y batería de hasta 28 días.", 850, "Smartwatches", images31);
        Product p5 = new Product("Samsung SSD 990 Pro 2TB", "Unidad de estado sólido NVMe PCIe 4.0 con velocidades de lectura/escritura de hasta 7450/6900 MB/s. Ideal para gaming y cargas de trabajo exigentes.", 220, "Ordenadores", images14);

        List<Product> productsUser4 = List.of(
            new Product("Razer BlackWidow V4", "Teclado mecánico para gaming con switches Razer Green, retroiluminación RGB personalizable y macros programables. Diseñado para ofrecer precisión y durabilidad en cada pulsación.", 150, "Otros", images11),
            new Product("Logitech MX Master 3S", "Ratón inalámbrico con sensor de 8000 DPI, diseñado para productividad y precisión. Cuenta con scroll electromagnético MagSpeed y conectividad Bluetooth y USB-C.", 120, "Otros", images12),
            new Product("Lenovo ThinkPad X1 Carbon", "Ultrabook empresarial con pantalla de 14” Full HD, procesador Intel Core i7, 16GB de RAM y SSD de 1TB. Construcción ligera en fibra de carbono y seguridad integrada con lector de huellas.", 1700, "Ordenadores", images13),
            p5,
            new Product("ASUS ROG Swift PG279QM", "Monitor gaming de 27” con resolución QHD, panel IPS de 240Hz y tecnología G-Sync. Ofrece una experiencia fluida y colores vibrantes para juegos competitivos.", 800, "Otros", images15),
            new Product("iPhone 15 Pro Max", "Smartphone con pantalla OLED de 6.7, procesador A17 Pro, triple cámara de 48MP y batería de larga duración.", 1300, "Móviles", images16),
            new Product("Samsung Galaxy S24 Ultra", "Pantalla Dynamic AMOLED 2X de 6.8. Snapdragon 8 Gen 3, S Pen integrado y cámara de 200MP.", 1400, "Móviles", images17),
            new Product("Google Pixel 8 Pro", "Pantalla LTPO OLED de 6.7, chip Tensor G3 y software avanzado de fotografía con IA.", 1100, "Móviles", images18),
            new Product("Xiaomi 13 Ultra", "Pantalla AMOLED de 6.73, Snapdragon 8 Gen 2, cámara Leica de 50MP y carga rápida de 90W.", 1200, "Móviles", images19),
            new Product("OnePlus 11 5G", "Pantalla AMOLED de 6.7, Snapdragon 8 Gen 2, batería de 5000mAh y carga rápida de 100W.", 800, "Móviles", images20),
            p3,
            new Product("Nothing Phone (2)", "Diseño transparente con iluminación Glyph, pantalla OLED de 6.7, Snapdragon 8+ Gen 1 y Android personalizado.", 700, "Móviles", images22),
            new Product("Realme GT5 Pro", "Snapdragon 8 Gen 3, pantalla AMOLED de 6.78, cámara de 50MP y carga rápida de 100W.", 750, "Móviles", images23),
            new Product("Dell XPS 15 9530", "Laptop con pantalla OLED 3.5K de 15.6, Intel Core i9, RTX 4060, 32GB de RAM y SSD de 1TB.", 2500, "Ordenadores", images24),
            new Product("MacBook Pro 16 M3 Max", "Chip M3 Max con 40 núcleos de GPU, pantalla Mini-LED, batería de larga duración y macOS optimizado.", 3500, "Ordenadores", images25),
            new Product("Asus ROG Strix Scar 17", "Laptop gaming con Ryzen 9 7945HX, RTX 4090, pantalla QHD 240Hz y diseño agresivo con RGB.", 3300, "Ordenadores", images26),
            p2,
            new Product("MSI Creator Z16P", "Laptop para creadores con pantalla táctil QHD+, Intel Core i9, RTX 4070 y 32GB de RAM.", 2900, "Ordenadores", images28),
            new Product("Apple Watch Ultra 2", "Reloj robusto con pantalla LTPO OLED de 49mm, resistencia al agua 100m, GPS dual y batería de hasta 36 horas.", 900, "Smartwatches", images29),
            new Product("Samsung Galaxy Watch 6 Classic", "Pantalla Super AMOLED de 1.5, bisel giratorio, funciones avanzadas de salud y compatibilidad con Samsung Pay.", 450, "Smartwatches", images30),
            p4,
            new Product("Google Pixel Watch 2", "Diseño minimalista con pantalla AMOLED, Wear OS optimizado, integración con Fitbit y funciones avanzadas de IA.", 400, "Smartwatches", images32)
        );
        



        // Assign owner to each product
        productsUser1.forEach(p -> p.setOwner(user1));
        productsUser2.forEach(p -> p.setOwner(user2));
        productsUser4.forEach(p -> p.setOwner(user4));

        // Save products in bd
        productRepository.saveAll(productsUser1);
        productRepository.saveAll(productsUser2);
        productRepository.saveAll(productsUser4);
        
        // Create chat
        Chat chat = new Chat(user1, user2, p1);
        chatRepository.save(chat);
        Message message1 = new Message("Hola quiero comprar la cámara", user1, chat);
        Message message2 = new Message("Si perfecto, ¿hoy a las 20:00 en Mostoles Central te viene bien?", user2, chat);
        Message message3 = new Message("Si, genial, luego nos vemos", user1, chat);
        Message message4 = new Message("Muchas gracias", user1, chat);
        messageRepository.save(message1);
        messageRepository.save(message2);
        messageRepository.save(message3);
        messageRepository.save(message4);
        Purchase purchase = new Purchase(p1, user1, user2);
        purchaseRepository.save(purchase);
        p1.setSold(true);
        productRepository.save(p1);
        Review review = new Review(user1,user2, "Muy buen servicio", 4);
        reviewRepository.save(review);

        //Create more purchases and reviews
        Purchase purchase2 = new Purchase(p2, user1, user4);
        purchaseRepository.save(purchase2);
        p2.setSold(true);
        productRepository.save(p2);
        Review review2 = new Review(user1,user4, "Muy majo y el producto en perfecto estado", 5);
        reviewRepository.save(review2);
        Chat chat2 = new Chat(user1, user4, p2);
        chatRepository.save(chat2);
        Message message5 = new Message("Hola quiero comprar el portatil", user1, chat2);
        Message message6 = new Message("Vale, ¿Te viene bien hoy a las 18:00 en Móstoles Central?", user4, chat2);
        Message message7 = new Message("Si, perfecto, nos vemos luego", user1, chat2);
        messageRepository.save(message5);
        messageRepository.save(message6);
        messageRepository.save(message7);

        Purchase purchase3 = new Purchase(p3, user1, user4);
        purchase3.setPurchaseDate(LocalDate.of(2025, 2, 14));
        purchaseRepository.save(purchase3);
        p3.setPublishDate(LocalDate.of(2025, 2, 14));
        p3.setSold(true);
        productRepository.save(p3);

        Purchase purchase4 = new Purchase(p4, user1, user4);
        purchase4.setPurchaseDate(LocalDate.of(2025, 1, 22));
        purchaseRepository.save(purchase4);
        p4.setPublishDate(LocalDate.of(2025, 1, 15));
        p4.setSold(true);
        productRepository.save(p4);

        Purchase purchase5 = new Purchase(p5, user2, user4);
        purchase5.setPurchaseDate(LocalDate.of(2025, 1, 15));
        purchaseRepository.save(purchase5);
        p5.setPublishDate(LocalDate.of(2025, 1, 6));
        p5.setSold(true);
        productRepository.save(p5);
        Review review3 = new Review(user2,user4, "Todo bien, todo correcto", 4);
        reviewRepository.save(review3);
        Chat chat3 = new Chat(user2, user4, p5);
        chatRepository.save(chat3);
        Message message8 = new Message("Buenas, quiero comprar la SSD", user2, chat3);
        Message message9 = new Message("Perfecto, ¿Hoy a las 19:00 en la URJC de Móstoles?", user4, chat3);
        Message message10 = new Message("Si, perfecto, ahí nos vemos", user2, chat3);
        message8.setSentAt(LocalDateTime.of(2025, 1, 15, 12, 0));
        message9.setSentAt(LocalDateTime.of(2025, 1, 15, 12, 5));
        message10.setSentAt(LocalDateTime.of(2025, 1, 15, 12, 7));
        messageRepository.save(message8);
        messageRepository.save(message9);
        messageRepository.save(message10);
    }

}
