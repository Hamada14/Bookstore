package server.database.entities;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import server.ResponseData;

public class User extends Entity implements Serializable {

	private static final long serialVersionUID = -6108052518548327564L;

	public static final String USER_TABLE = "USERS";

	private static final String NEW_USER_QUERY = "Insert into %s(USER_NAME, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD_SHA1, PHONE_NUMBER, ADDRESS)"
			+ "VALUES (%s, %s, %s, %s, SHA1(%s), %s, %s);";

	private static final String GET_USER_BY_EMAIL = "Select * from %s where EMAIL = %s;";
	private static final String GET_USER_BY_USER_NAME = "Select * from %s where USER_NAME = %s";

	private static final String SHORTER_THAN_ALLOWED_ERROR = "Field can't be less than";
	private static final String LONGER_THAN_ALLOWED_ERROR = "Field can't be longer than";

	private static final String DUPLICATE_EMAIL_ERROR = "User with this email already registered";
	private static final String DUPLICATE_USER_NAME_ERROR = "User with this user name already registered";

	private String userName;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String address;
	private String phoneNumber;
	private boolean isManager;

	public User() {

	}

	public User(String userName, String firstName, String lastName, String email, String password, String address,
			String phoneNumber, boolean isManager) {
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.isManager = isManager;
	}

	@Override
	public String toString() {
		return "User [firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + ", password=" + password
				+ ", address=" + address + ", phoneNumber=" + phoneNumber + ", isManager=" + isManager + "]";
	}

	public void registerUser(Connection connection, String tableName) throws SQLException {
		Statement stat = connection.createStatement();
		String insertUserQuery = createInsertUserQuery(tableName);
		stat.execute(insertUserQuery);
	}

	public String validateData() {
		StringBuilder sb = new StringBuilder();
		List<String> attributes = Arrays.asList("User Name", "First Name", "Last Name", "Email", "Password", "Address",
				"Phone Number");
		List<String> values = Arrays.asList(userName, firstName, lastName, email, password, address, phoneNumber);
		List<Integer> maxLength = Arrays.asList(20, 20, 20, 20, 40, 50, 15);
		List<Integer> minLength = Arrays.asList(5, 5, 5, 5, 8, 5, 8);
		for (int i = 0; i < attributes.size(); i++) {
			String value = values.get(i);
			String attribute = attributes.get(i);
			int attributeMinLength = minLength.get(i);
			int attributeMaxLength = maxLength.get(i);
			if (value.length() < attributeMinLength) {
				sb.append(attribute + " " + SHORTER_THAN_ALLOWED_ERROR + " " + attributeMinLength);
			} else if (value.length() > attributeMaxLength) {
				sb.append(attribute + " " + LONGER_THAN_ALLOWED_ERROR + " " + attributeMaxLength);
			}
		}
		return sb.length() != 0 ? sb.toString() : null;
	}

	private String createInsertUserQuery(String tableName) {
		String query = String.format(NEW_USER_QUERY, tableName, addQuotes(userName), addQuotes(firstName),
				addQuotes(lastName), addQuotes(email), addQuotes(password), addQuotes(phoneNumber), addQuotes(address));
		return query;
	}

	public static ResponseData addNewUser(User user, Connection connection) {
		ResponseData rs = new ResponseData();
		String userErrors = user.validateData();
		try {
			if (userErrors != null) {
				rs.setError(userErrors);
			} else if (user.isAlreadyRegisteredUserName(connection)) {
				rs.setError(DUPLICATE_USER_NAME_ERROR);
			} else if (user.isAlreadyRegisteredEmail(connection)) {
				rs.setError(DUPLICATE_EMAIL_ERROR);
			} else {
				user.registerUser(connection, USER_TABLE);
			}
		} catch (SQLException e) {
			rs.setError("Couldn't execute query");
			e.printStackTrace();
		}
		return rs;
	}

	private boolean isAlreadyRegisteredEmail(Connection connection) throws SQLException {
		return isAlreadyRegistered(connection, GET_USER_BY_EMAIL, email);
	}

	private boolean isAlreadyRegisteredUserName(Connection connection) throws SQLException {
		return isAlreadyRegistered(connection, GET_USER_BY_USER_NAME, userName);
	}

	private boolean isAlreadyRegistered(Connection connection, String selectionType, String attribute)
			throws SQLException {
		Statement st = connection.createStatement();
		String query = String.format(selectionType, USER_TABLE, addQuotes(attribute));
		ResultSet rs = st.executeQuery(query);
		return rs.next();
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	public boolean isManager() {
		return isManager;
	}

	public void setManager(boolean isManager) {
		this.isManager = isManager;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}
