package es.grupo04.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Purchase {
    private String title;

    @Column(columnDefinition = "TEXT")
	private String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
