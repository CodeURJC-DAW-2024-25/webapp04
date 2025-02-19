package es.grupo04.backend.service;

import java.io.IOException;
import java.net.URISyntaxException;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import es.grupo04.backend.model.Product;
import es.grupo04.backend.repository.ProductRepository;
import es.grupo04.backend.repository.UserRepository;
import jakarta.annotation.PostConstruct;

@Service
public class DataBaseInitializer {

}