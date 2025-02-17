package es.grupo04.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Purchase {
    private String title;

    @Column(columnDefinition = "TEXT")
	private String description;
}
