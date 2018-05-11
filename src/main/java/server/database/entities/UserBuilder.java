package server.database.entities;

public class UserBuilder {

	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String address;
	private String phoneNumber;
	private boolean isManager;

	public UserBuilder() {
		this.isManager = false;
	}

	public User buildUser() {
		return new User(firstName, lastName, email, password, address, phoneNumber, isManager);
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}
