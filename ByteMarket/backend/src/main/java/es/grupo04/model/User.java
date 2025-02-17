package es.grupo04.model;

//@Entity(name = "UserTable")
public class User {

	/*@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;*/

	private String name;

	private String encodedPassword;


	public User() {
	}

	public User(String name, String encodedPassword) {
		this.name = name;
		this.encodedPassword = encodedPassword;
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

}