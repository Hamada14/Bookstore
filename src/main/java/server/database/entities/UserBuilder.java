package server.database.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
	private static final String NOT_MATCH_FORMAT = "Field doesn't match specified format";

	private static final String USER_NAME_FORMAT = "^[a-zA-Z][a-zA-Z0-9\\-_]+";
	private static final int USER_NAME_MIN_LEN = 3;
	private static final int USER_NAME_MAX_LEN = 50;

	private static final String NAME_FORMAT = "^([a-zA-Z]+\\s?)+";
	private static final int NAME_MIN_LENGTH = 3;
	private static final int NAME_MAX_LENGTH = 50;

	private static final String PASSWORD_FORMAT = "^.*";
	private static final int PASSWORD_MIN_LEN = 5;
	private static final int PASSWORD_MAX_LEN = 50;

	private static final String PHONE_NUMBER_FORMAT = "(\\d{14,14})|\\d{12,12}";
	private static final int PHONE_NUMBER_MIN_LEN = 9;
	private static final int PHONE_NUMBER_MAX_LEN = 11;

	private static final String EMAIL_FORMAT = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
	private static final int EMAIL_MIN_LEN = 5;
	private static final int EMAIL_MAX_LEN = 50;

	private static final String STREET_FORMAT = "^.*";
	private static final int STREET_MIN_LEN = 5;
	private static final int STREET_MAX_LEN = 50;

	private static final String CITY_FORMAT = "^([a-zA-Z]+\\s)*([a-zA-Z]+)";
	private static final int CITY_MIN_LEN = 5;
	private static final int CITY_MAX_LEN = 50;

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
		List<String> errors = new ArrayList<>();
		errors.addAll(validateUserName());
		errors.addAll(validateFirstName());
		errors.addAll(validateLastName());
		errors.addAll(validateEmail());
		errors.addAll(validatePassword());
		errors.addAll(validatePhoneNumber());
		errors.addAll(validateStreet());
		errors.addAll(validateCity());
		errors.addAll(validateCountry());
		String combinedErrors = errors.stream().filter(error -> error != null).map(msg -> "* " + msg)
				.collect(Collectors.joining(System.lineSeparator()));
		return combinedErrors.length() == 0 ? null : combinedErrors;
	}

	private List<String> validateUserName() {
		return evaluateDataChecks(userName, USER_NAME, USER_NAME_MIN_LEN, USER_NAME_MAX_LEN, USER_NAME_FORMAT);
	}

	private List<String> validateFirstName() {
		return validateName(firstName, FIRST_NAME);
	}

	private List<String> validateLastName() {
		return validateName(lastName, LAST_NAME);
	}

	private List<String> validateName(final String value, final String attribute) {
		return evaluateDataChecks(value, attribute, NAME_MIN_LENGTH, NAME_MAX_LENGTH, NAME_FORMAT);
	}

	private List<String> validatePassword() {
		return evaluateDataChecks(password, PASSWORD, PASSWORD_MIN_LEN, PASSWORD_MAX_LEN, PASSWORD_FORMAT);
	}

	private List<String> validatePhoneNumber() {
		return evaluateDataChecks(phoneNumber, PHONE_NUMBER, PHONE_NUMBER_MIN_LEN, PHONE_NUMBER_MAX_LEN,
				PHONE_NUMBER_FORMAT);
	}

	private List<String> validateStreet() {
		return evaluateDataChecks(street, STREET, STREET_MIN_LEN, STREET_MAX_LEN, STREET_FORMAT);
	}

	private List<String> validateCity() {
		return evaluateDataChecks(city, CITY, CITY_MIN_LEN, CITY_MAX_LEN, CITY_FORMAT);
	}

	private List<String> validateCountry() {
		if(country.length() == 0 || !User.getValidCountries().contains(country)) {
			return Arrays.asList(COUNTRY + " " + CANNOT_BE_EMPTY_ERROR);
		}
		return Arrays.asList();
	}

	private Collection<? extends String> validateEmail() {
		return evaluateDataChecks(email, EMAIL, EMAIL_MIN_LEN, EMAIL_MAX_LEN, EMAIL_FORMAT);
	}

	private List<String> evaluateDataChecks(String val, String attribute, int minLen, int maxLen, String format) {
		String lengthError = checkAttributeLength(val, attribute, minLen, maxLen);
		String formatError = checkAttributeFormat(val, attribute, format);
		if(val.length() == 0) {
			return Arrays.asList(lengthError);
		}
		return formatError == null ? Arrays.asList(lengthError) : Arrays.asList(formatError);
	}

	private String checkAttributeLength(String attributeValue, String attribute, int minLength, int maxLength) {
		if (attributeValue.length() == 0 && minLength > 0) {
			return (attribute + " " + CANNOT_BE_EMPTY_ERROR);
		} else if (attributeValue.length() < minLength) {
			return (attribute + " " + SHORTER_THAN_ALLOWED_ERROR + " " + minLength + " Characters");
		} else if (attributeValue.length() > maxLength) {
			return (attribute + " " + LONGER_THAN_ALLOWED_ERROR + " " + maxLength + " Characters");
		}
		return null;
	}

	private String checkAttributeFormat(String attributeValue, String attribute, String format) {
		Pattern pattern = Pattern.compile(format);
		Matcher matcher = pattern.matcher(attributeValue);
		boolean matches = matcher.matches();
		if (!matches) {
			return (attribute + " " + NOT_MATCH_FORMAT);
		}
		return null;
	}
}
