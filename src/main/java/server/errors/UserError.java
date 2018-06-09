package server.errors;

public enum UserError {
	
	INVALID_USER_NAME("ERROR: invalid username or already used username."),
	INVALID_EMAIL("ERROR: invalid email or already used email."),
	INVALID_NAME("ERROR: invalid name."),
	INVALID_PASSWORD("ERROR: invalid password"),
	INVALID_STREET("ERROR: invalid street"),
	INVALID_CITY("ERROR: invalid city"),
	INVALID_PHONE_NUMBER("ERROR: invalid phone number"),
	INCORRECT_USER_NAME("ERROR: incorrect username"),
	INCORRECT_PASSWORD("ERROR: incorrect password");
	
	
	private final String text;

	UserError(final String text) {
		this.text = text;
	}

	public String toString() {
		return text;
	}
}
