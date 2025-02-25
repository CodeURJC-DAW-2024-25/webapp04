package es.grupo04.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.grupo04.backend.model.Image;
import es.grupo04.backend.repository.ImageRepository;

@Service
public class ImageService {

	@Autowired
	private ImageRepository repository;

	public Optional<Image> findById(long id) {
		return repository.findById(id);
	}
	
	public boolean exist(long id) {
		return repository.existsById(id);
	}

	public List<Image> findAll() {
		return repository.findAll();
	}

	public void save(Image image) {
		repository.save(image);
	}

	public void delete(long id) {
		repository.deleteById(id);
	}
}
