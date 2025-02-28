package es.grupo04.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.grupo04.backend.model.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
    
}
