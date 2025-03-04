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



import es.grupo04.backend.model.Chat;
import es.grupo04.backend.model.Image;
import es.grupo04.backend.model.Message;
import es.grupo04.backend.model.Product;
import es.grupo04.backend.model.Purchase;
import es.grupo04.backend.model.Review;
import es.grupo04.backend.model.User;
import es.grupo04.backend.repository.ChatRepository;
import es.grupo04.backend.repository.ImageRepository;
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
    private ImageRepository ImageRepository;

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

        // CAMERA
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

        // KEYBOARD
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
        
        // EARPHONES
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

        // PC 
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

        // PRODUCTS 11-32
        Path imagePath24 = Paths.get("src", "main", "resources", "static", "images", "tecladoRazer.png");
        File imageFile24 = imagePath24.toFile();
        byte[] imageData24 = Files.readAllBytes(imageFile24.toPath());

        Path imagePath25 = Paths.get("src", "main", "resources", "static", "images", "ratonLogitech1.png");
        File imageFile25 = imagePath25.toFile();
        byte[] imageData25 = Files.readAllBytes(imageFile25.toPath());

        Path imagePath26 = Paths.get("src", "main", "resources", "static", "images", "lenovo1.png");
        File imageFile26 = imagePath26.toFile();
        byte[] imageData26 = Files.readAllBytes(imageFile26.toPath());

        Path imagePath27 = Paths.get("src", "main", "resources", "static", "images", "ssd1.png");
        File imageFile27 = imagePath27.toFile();
        byte[] imageData27 = Files.readAllBytes(imageFile27.toPath());

        Path imagePath28 = Paths.get("src", "main", "resources", "static", "images", "monitor.png");
        File imageFile28 = imagePath28.toFile();
        byte[] imageData28 = Files.readAllBytes(imageFile28.toPath());

        Path imagePath29 = Paths.get("src", "main", "resources", "static", "images", "iphoneProMax.png");
        File imageFile29 = imagePath29.toFile();
        byte[] imageData29 = Files.readAllBytes(imageFile29.toPath());

        Path imagePath30 = Paths.get("src", "main", "resources", "static", "images", "samsung24Ultra.png");
        File imageFile30 = imagePath30.toFile();
        byte[] imageData30 = Files.readAllBytes(imageFile30.toPath());

        Path imagePath31 = Paths.get("src", "main", "resources", "static", "images", "googlePixelPro.png");
        File imageFile31 = imagePath31.toFile();
        byte[] imageData31 = Files.readAllBytes(imageFile31.toPath());

        Path imagePath32 = Paths.get("src", "main", "resources", "static", "images", "xiaomi13Ultra.png");
        File imageFile32 = imagePath32.toFile();
        byte[] imageData32 = Files.readAllBytes(imageFile32.toPath());

        Path imagePath33 = Paths.get("src", "main", "resources", "static", "images", "onePlus.png");
        File imageFile33 = imagePath33.toFile();
        byte[] imageData33 = Files.readAllBytes(imageFile33.toPath());

        Path imagePath34 = Paths.get("src", "main", "resources", "static", "images", "sonyXPeria.png");
        File imageFile34 = imagePath34.toFile();
        byte[] imageData34 = Files.readAllBytes(imageFile34.toPath());

        Path imagePath35 = Paths.get("src", "main", "resources", "static", "images", "nothingPhone.png");
        File imageFile35 = imagePath35.toFile();
        byte[] imageData35 = Files.readAllBytes(imageFile35.toPath());

        Path imagePath36 = Paths.get("src", "main", "resources", "static", "images", "realmeGT.png");
        File imageFile36 = imagePath36.toFile();
        byte[] imageData36 = Files.readAllBytes(imageFile36.toPath());

        Path imagePath37 = Paths.get("src", "main", "resources", "static", "images", "dellXP.png");
        File imageFile37 = imagePath37.toFile();
        byte[] imageData37 = Files.readAllBytes(imageFile37.toPath());

        Path imagePath38 = Paths.get("src", "main", "resources", "static", "images", "macbookPro.png");
        File imageFile38 = imagePath38.toFile();
        byte[] imageData38 = Files.readAllBytes(imageFile38.toPath());

        Path imagePath39 = Paths.get("src", "main", "resources", "static", "images", "asusRog.png");
        File imageFile39 = imagePath39.toFile();
        byte[] imageData39 = Files.readAllBytes(imageFile39.toPath());

        Path imagePath40 = Paths.get("src", "main", "resources", "static", "images", "hpSpectre.png");
        File imageFile40 = imagePath40.toFile();
        byte[] imageData40 = Files.readAllBytes(imageFile40.toPath());

        Path imagePath41 = Paths.get("src", "main", "resources", "static", "images", "msiCreator.png");
        File imageFile41 = imagePath41.toFile();
        byte[] imageData41 = Files.readAllBytes(imageFile41.toPath());

        Path imagePath42 = Paths.get("src", "main", "resources", "static", "images", "appleWatch.png");
        File imageFile42 = imagePath42.toFile();
        byte[] imageData42 = Files.readAllBytes(imageFile42.toPath());

        Path imagePath43 = Paths.get("src", "main", "resources", "static", "images", "galaxyWatch.png");
        File imageFile43 = imagePath43.toFile();
        byte[] imageData43 = Files.readAllBytes(imageFile43.toPath());

        Path imagePath44 = Paths.get("src", "main", "resources", "static", "images", "garmingFenix.png");
        File imageFile44 = imagePath44.toFile();
        byte[] imageData44 = Files.readAllBytes(imageFile44.toPath());

        Path imagePath45 = Paths.get("src", "main", "resources", "static", "images", "googlePixelWatch.png");
        File imageFile45 = imagePath45.toFile();
        byte[] imageData45 = Files.readAllBytes(imageFile45.toPath());

        Blob imageBlob24 = new SerialBlob(imageData24);
        Image image24 = new Image(imageBlob24);

        Blob imageBlob25 = new SerialBlob(imageData25);
        Image image25 = new Image(imageBlob25);

        Blob imageBlob26 = new SerialBlob(imageData26);
        Image image26 = new Image(imageBlob26);

        Blob imageBlob27 = new SerialBlob(imageData27);
        Image image27 = new Image(imageBlob27);

        Blob imageBlob28 = new SerialBlob(imageData28);
        Image image28 = new Image(imageBlob28);

        Blob imageBlob29 = new SerialBlob(imageData29);
        Image image29 = new Image(imageBlob29);

        Blob imageBlob30 = new SerialBlob(imageData30);
        Image image30 = new Image(imageBlob30);

        Blob imageBlob31 = new SerialBlob(imageData31);
        Image image31 = new Image(imageBlob31);

        Blob imageBlob32 = new SerialBlob(imageData32);
        Image image32 = new Image(imageBlob32);

        Blob imageBlob33 = new SerialBlob(imageData33);
        Image image33 = new Image(imageBlob33);

        Blob imageBlob34 = new SerialBlob(imageData34);
        Image image34 = new Image(imageBlob34);

        Blob imageBlob35 = new SerialBlob(imageData35);
        Image image35 = new Image(imageBlob35);

        Blob imageBlob36 = new SerialBlob(imageData36);
        Image image36 = new Image(imageBlob36);

        Blob imageBlob37 = new SerialBlob(imageData37);
        Image image37 = new Image(imageBlob37);

        Blob imageBlob38 = new SerialBlob(imageData38);
        Image image38 = new Image(imageBlob38);

        Blob imageBlob39 = new SerialBlob(imageData39);
        Image image39 = new Image(imageBlob39);

        Blob imageBlob40 = new SerialBlob(imageData40);
        Image image40 = new Image(imageBlob40);

        Blob imageBlob41 = new SerialBlob(imageData41);
        Image image41 = new Image(imageBlob41);

        Blob imageBlob42 = new SerialBlob(imageData42);
        Image image42 = new Image(imageBlob42);

        Blob imageBlob43 = new SerialBlob(imageData43);
        Image image43 = new Image(imageBlob43);

        Blob imageBlob44 = new SerialBlob(imageData44);
        Image image44 = new Image(imageBlob44);

        Blob imageBlob45 = new SerialBlob(imageData45);
        Image image45 = new Image(imageBlob45);

        ArrayList<Image> images11 = new ArrayList<>();
        images11.add(image24);

        ArrayList<Image> images12 = new ArrayList<>();
        images12.add(image25);

        ArrayList<Image> images13 = new ArrayList<>();
        images13.add(image26);

        ArrayList<Image> images14 = new ArrayList<>();
        images14.add(image27);

        ArrayList<Image> images15 = new ArrayList<>();
        images15.add(image28);

        ArrayList<Image> images16 = new ArrayList<>();
        images16.add(image29);

        ArrayList<Image> images17 = new ArrayList<>();
        images17.add(image30);

        ArrayList<Image> images18 = new ArrayList<>();
        images18.add(image31);

        ArrayList<Image> images19 = new ArrayList<>();
        images19.add(image32);

        ArrayList<Image> images20 = new ArrayList<>();
        images20.add(image33);

        ArrayList<Image> images21 = new ArrayList<>();
        images21.add(image34);

        ArrayList<Image> images22 = new ArrayList<>();
        images22.add(image35);

        ArrayList<Image> images23 = new ArrayList<>();
        images23.add(image36);

        ArrayList<Image> images24 = new ArrayList<>();
        images24.add(image37);

        ArrayList<Image> images25 = new ArrayList<>();
        images25.add(image38);

        ArrayList<Image> images26 = new ArrayList<>();
        images26.add(image39);

        ArrayList<Image> images27 = new ArrayList<>();
        images27.add(image40);

        ArrayList<Image> images28 = new ArrayList<>();
        images28.add(image41);

        ArrayList<Image> images29 = new ArrayList<>();
        images29.add(image42);

        ArrayList<Image> images30 = new ArrayList<>();
        images30.add(image43);

        ArrayList<Image> images31 = new ArrayList<>();
        images31.add(image44);

        ArrayList<Image> images32 = new ArrayList<>();
        images32.add(image45);


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

        List<Product> productsUser4 = List.of(
            new Product("Razer BlackWidow V4", "Teclado mecánico para gaming con switches Razer Green, retroiluminación RGB personalizable y macros programables. Diseñado para ofrecer precisión y durabilidad en cada pulsación.", 150, "Otros", images11),
            new Product("Logitech MX Master 3S", "Ratón inalámbrico con sensor de 8000 DPI, diseñado para productividad y precisión. Cuenta con scroll electromagnético MagSpeed y conectividad Bluetooth y USB-C.", 120, "Otros", images12),
            new Product("Lenovo ThinkPad X1 Carbon", "Ultrabook empresarial con pantalla de 14” Full HD, procesador Intel Core i7, 16GB de RAM y SSD de 1TB. Construcción ligera en fibra de carbono y seguridad integrada con lector de huellas.", 1700, "Ordenadores", images13),
            new Product("Samsung SSD 990 Pro 2TB", "Unidad de estado sólido NVMe PCIe 4.0 con velocidades de lectura/escritura de hasta 7450/6900 MB/s. Ideal para gaming y cargas de trabajo exigentes.", 220, "Ordenadores", images14),
            new Product("ASUS ROG Swift PG279QM", "Monitor gaming de 27” con resolución QHD, panel IPS de 240Hz y tecnología G-Sync. Ofrece una experiencia fluida y colores vibrantes para juegos competitivos.", 800, "Otros", images15),
            new Product("iPhone 15 Pro Max", "Smartphone con pantalla OLED de 6.7, procesador A17 Pro, triple cámara de 48MP y batería de larga duración.", 1300, "Móviles", images16),
            new Product("Samsung Galaxy S24 Ultra", "Pantalla Dynamic AMOLED 2X de 6.8. Snapdragon 8 Gen 3, S Pen integrado y cámara de 200MP.", 1400, "Móviles", images17),
            new Product("Google Pixel 8 Pro", "Pantalla LTPO OLED de 6.7, chip Tensor G3 y software avanzado de fotografía con IA.", 1100, "Móviles", images18),
            new Product("Xiaomi 13 Ultra", "Pantalla AMOLED de 6.73, Snapdragon 8 Gen 2, cámara Leica de 50MP y carga rápida de 90W.", 1200, "Móviles", images19),
            new Product("OnePlus 11 5G", "Pantalla AMOLED de 6.7, Snapdragon 8 Gen 2, batería de 5000mAh y carga rápida de 100W.", 800, "Móviles", images20),
            new Product("Sony Xperia 1 V", "Pantalla 4K HDR OLED de 6.5, Snapdragon 8 Gen 2, triple cámara con sensor Exmor T y audio Hi-Res.", 1200, "Móviles", images21),
            new Product("Nothing Phone (2)", "Diseño transparente con iluminación Glyph, pantalla OLED de 6.7, Snapdragon 8+ Gen 1 y Android personalizado.", 700, "Móviles", images22),
            new Product("Realme GT5 Pro", "Snapdragon 8 Gen 3, pantalla AMOLED de 6.78, cámara de 50MP y carga rápida de 100W.", 750, "Móviles", images23),
            new Product("Dell XPS 15 9530", "Laptop con pantalla OLED 3.5K de 15.6, Intel Core i9, RTX 4060, 32GB de RAM y SSD de 1TB.", 2500, "Ordenadores", images24),
            new Product("MacBook Pro 16 M3 Max", "Chip M3 Max con 40 núcleos de GPU, pantalla Mini-LED, batería de larga duración y macOS optimizado.", 3500, "Ordenadores", images25),
            new Product("Asus ROG Strix Scar 17", "Laptop gaming con Ryzen 9 7945HX, RTX 4090, pantalla QHD 240Hz y diseño agresivo con RGB.", 3300, "Ordenadores", images26),
            new Product("HP Spectre x360 14", "Convertible premium con pantalla OLED de 14, Intel Core i7, 16GB RAM y SSD de 1TB.", 1800, "Ordenadores", images27),
            new Product("MSI Creator Z16P", "Laptop para creadores con pantalla táctil QHD+, Intel Core i9, RTX 4070 y 32GB de RAM.", 2900, "Ordenadores", images28),
            new Product("Apple Watch Ultra 2", "Reloj robusto con pantalla LTPO OLED de 49mm, resistencia al agua 100m, GPS dual y batería de hasta 36 horas.", 900, "Smartwatches", images29),
            new Product("Samsung Galaxy Watch 6 Classic", "Pantalla Super AMOLED de 1.5, bisel giratorio, funciones avanzadas de salud y compatibilidad con Samsung Pay.", 450, "Smartwatches", images30),
            new Product("Garmin Fenix 7X Pro", "Smartwatch multideporte con GPS de precisión, mapas topográficos, monitorización avanzada de salud y batería de hasta 28 días.", 850, "Smartwatches", images31),
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
    }

}
