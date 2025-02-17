package es.grupo04.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import es.grupo04.model.Product;
import es.grupo04.model.Shop;
import es.grupo04.model.User;
import es.grupo04.repository.ProductRepository;
import es.grupo04.repository.ShopRepository;
import es.grupo04.repository.UserRepository;
import jakarta.annotation.PostConstruct;

@Service
public class DataBaseInitializer {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ShopRepository shopRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostConstruct
	public void init() throws IOException, URISyntaxException {

		// Sample Shops
		Shop shop1 = new Shop("Casa del libro", "C/ Falsa 123");
		Shop shop2 = new Shop("FNAC", "C/ Falsa 456");
		Shop shop3 = new Shop("Librería de la plaza", "C/ Falsa 012");

		shopRepository.save(shop1);
		shopRepository.save(shop2);
		shopRepository.save(shop3);

		// Sample products

		Product product1 = new Product("SUEÑOS DE ACERO Y NEON",
				"Los personajes que protagonizan este relato sobreviven en una sociedad en decadencia a la que, no obstante, lograrán devolver la posibilidad de un futuro. Año 2484. En un mundo dominado por las grandes corporaciones, solo un hombre, Jordi Thompson, detective privado deslenguado y vividor, pero de gran talento y sentido d...",
				new ArrayList<>(Arrays.asList(shop1, shop2)));

                setProductImage(product1, "/public/defaul-pfp.jpg");
		        productRepository.save(product1);

		Product product2 = new Product("LA VIDA SECRETA DE LA MENTE",
				"La vida secreta de la mentees un viaje especular que recorre el cerebro y el pensamiento: se trata de descubrir nuestra mente para entendernos hasta en los más pequeños rincones que componen lo que somos, cómo forjamos las ideas en los primeros días de vida, cómo damos forma a las decisiones que nos constituyen, cómo soñamos y cómo imaginamos, por qué sentimos ciertas emociones hacia los demás, cómo los demás influyen en nosotros, y cómo el cerebro se transforma y, con él, lo que somos.",
				new ArrayList<>(Arrays.asList(shop2)));

                setProductImage(product2, "/public/defaul-pfp.jpg");
		        productRepository.save(product2);

		        /*productRepository.save(new Product("CASI SIN QUERER",
				"El amor algunas veces es tan complicado como impredecible. Pero al final lo que más valoramos son los detalles más simples, los más bonitos, los que llegan sin avisar. Y a la hora de escribir sobre sentimientos, no hay nada más limpio que hacerlo desde el corazón. Y eso hace Defreds en este libro.",
				new ArrayList<>(Arrays.asList(shop1))));

		        productRepository.save(new Product("TERMINAMOS Y OTROS POEMAS SIN TERMINAR",
				"Recopilación de nuevos poemas, textos en prosa y pensamientos del autor. Un sabio dijo una vez: «Pocas cosas hipnotizan tanto en este mundo como una llama y como la luna, será porque no podemos cogerlas o porque nos iluminan en la penumbra». Realmente no sé si alguien dijo esta cita o me la acabo de inventar pero deberían de haberla escrito porque el poder hipnótico que ejercen esa mujer de rojo y esa dama blanca sobre el ser humano es digna de estudio.",
				new ArrayList<>(Arrays.asList(shop2))));

		        productRepository.save(new Product("LA LEGIÓN PERDIDA",
				"En el año 53 a. C. el cónsul Craso cruzó el Éufrates para conquistar Oriente, pero su ejército fue destrozado en Carrhae. Una legión entera cayó prisionera de los partos. Nadie sabe a ciencia cierta qué pasó con aquella legión perdida.150 años después, Trajano está a punto de volver a cruzar el Éufrates. ...",
				new ArrayList<>(Arrays.asList(shop1, shop2))));*/

		// Sample users

		userRepository.save(new User("user", passwordEncoder.encode("pass"), "USER"));
		userRepository.save(new User("admin", passwordEncoder.encode("adminpass"), "USER", "ADMIN"));
	}

	public void setProductImage(Product product, String classpathResource) throws IOException {
		product.setImage(true);
		Resource image = new ClassPathResource(classpathResource);
		product.setImageFile(BlobProxy.generateProxy(image.getInputStream(), image.contentLength()));
	}
}