package es.grupo04.model;

<<<<<<< HEAD
import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
=======
import java.sql.Blob;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
>>>>>>> f8ec2fb95a0e5346df47fb9b9383cae8159fa35b

@Entity(name = "UserTable")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String name;
	private String mail;
	private String encodedPassword;
	
	@Lob
	private Blob imageFile;

	@ElementCollection(fetch = FetchType.EAGER)
	private List<String> roles;

	public User() {
	}

<<<<<<< HEAD
	public User(String name, String encodedPassword, String... roles) {
=======
	public User(String name, String encodedPassword, String mail) {
		this.mail = mail;
>>>>>>> f8ec2fb95a0e5346df47fb9b9383cae8159fa35b
		this.name = name;
		this.encodedPassword = encodedPassword;
		this.roles = List.of(roles);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEncodedPassword() {
		return encodedPassword;
	}

	public void setEncodedPassword(String encodedPassword) {
		this.encodedPassword = encodedPassword;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

}