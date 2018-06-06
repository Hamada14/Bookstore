package server.database.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserBuilder implements Serializable {

	private static final long serialVersionUID = 58362545449294450L;

	private static final String USER_NAME = "User name";
	private static final String FIRST_NAME = "First name";
	private static final String LAST_NAME = "Last name";
	private static final String EMAIL = "Email";
	private static final String PASSWORD = "Password";
	private static final String PHONE_NUMBER = "Phone number";
	private static final String STREET = "Street";
	private static final String CITY = "City";
	private static final String COUNTRY = "Country";

	private static final String SHORTER_THAN_ALLOWED_ERROR = "Field can't be less than";
	private static final String LONGER_THAN_ALLOWED_ERROR = "Field can't be longer than";
	private static final String CANNOT_BE_EMPTY_ERROR = "Field can't be empty";
	
	private String userName;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String phoneNumber;
	private String street;
	private String city;
	private String country;

	public UserBuilder() {
	}

	public User buildUser() {
		return new User(userName, firstName, lastName, email, password, phoneNumber, street, city, country);
	}

	public String validateData() {
		List<String> attributes = Arrays.asList(USER_NAME, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, PHONE_NUMBER, STREET,
				CITY, COUNTRY);
		List<String> values = Arrays.asList(userName, firstName, lastName, email, password, phoneNumber, street, city,
				country);
		List<Integer> maxLengths = Arrays.asList(50, 50, 50, 50, 50, 20, 50, 50, 50);
		List<Integer> minLengths = Arrays.asList(3, 3, 3, 7, 7, 7, 7, 3, 3);
		List<String> errors = new ArrayList<>();
		for (int i = 0; i < attributes.size(); i++) {
			String error = checkAttributeLength(values.get(i), attributes.get(i), minLengths.get(i), maxLengths.get(i));
			errors.add(error);
		}
		String combinedErrors = errors.stream().filter(item -> item != null)
				.map(msg -> "* " + msg)
				.collect(Collectors.joining(System.lineSeparator()));
		return combinedErrors.length() == 0 ? null : combinedErrors;
	}

	private String checkAttributeLength(String attributeValue, String attribute, int minLength, int maxLength) {
		if(attributeValue.length() == 0 && minLength > 0) {
			return (attribute + " " + CANNOT_BE_EMPTY_ERROR);
		} else if (attributeValue.length() < minLength) {
			return (attribute + " " + SHORTER_THAN_ALLOWED_ERROR + " " + minLength);
		} else if (attributeValue.length() > maxLength) {
			return (attribute + " " + LONGER_THAN_ALLOWED_ERROR + " " + maxLength);
		}
		return null;
	}
}
