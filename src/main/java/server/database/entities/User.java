package server.database.entities;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import server.ResponseData;

@Getter
@Setter
public class User implements Serializable {

	private static final long serialVersionUID = -6108052518548327564L;

	public static final String USER_TABLE = "CUSTOMER";

	private static final String NEW_USER_QUERY = "Insert into %s(USER_NAME, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD_SHA1, PHONE_NUMBER, STREET, CITY, COUNTRY)"
			+ "VALUES (?, ?, ?, ?, SHA1(?), ?, ?, ?, ?);";
	private static final int USER_NAME_INDEX = 1;
	private static final int FIRST_NAME_INDEX = 2;
	private static final int LAST_NAME_INDEX = 3;
	private static final int EMAIL_INDEX = 4;
	private static final int PASSWORD_INDEX = 5;
	private static final int PHONE_NUMBER_INDEX = 6;
	private static final int STREET_INDEX = 7;
	private static final int CITY_INDEX = 8;
	private static final int COUNTRY_INDEX = 9;

	private static final String GET_USER_BY_EMAIL = "Select * from %s where EMAIL = ?;";
	private static final String GET_USER_BY_USER_NAME = "Select * from %s where USER_NAME = ?;";

	private static final String DUPLICATE_EMAIL_ERROR = "User with this email already registered";
	private static final String DUPLICATE_USER_NAME_ERROR = "User with this user name already registered";

	private Identity identity;

	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;

	private String street;
	private String city;
	private String country;

	public User() {
		this.identity = new Identity();
	}

	public User(String userName, String firstName, String lastName, String email, String password, String phoneNumber,
			String street, String city, String country) {
		this.identity = new Identity(userName, password);
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.street = street;
		this.city = city;
		this.country = country;
	}

	public User(ResultSet rs) throws SQLException {
		this.identity = new Identity(rs.getString(USER_NAME_INDEX), rs.getString(USER_NAME_INDEX));
		this.firstName = rs.getString(FIRST_NAME_INDEX);
		this.lastName = rs.getString(LAST_NAME_INDEX);
		this.phoneNumber = rs.getString(PHONE_NUMBER_INDEX);
		this.email = rs.getString(EMAIL_INDEX);
		this.street = rs.getString(STREET_INDEX);
		this.city = rs.getString(CITY_INDEX);
		this.country = rs.getString(COUNTRY_INDEX);
	}

	@Override
	public String toString() {
		return "User [firstName=" + firstName + ", lastName=" + lastName + ", userName=" + identity.getUserName()
				+ ", email=" + email + ", password=" + identity.getPassword() + ", phoneNumber=" + phoneNumber;
	}

	public void registerUser(Connection connection, String tableName) throws SQLException {
		PreparedStatement registerUserQuery = createInsertUserQuery(connection, tableName);
		registerUserQuery.execute();
	}

	private PreparedStatement createInsertUserQuery(Connection con, String tableName) throws SQLException {
		String query = String.format(NEW_USER_QUERY, tableName);
		PreparedStatement st = con.prepareStatement(query);
		st.setString(USER_NAME_INDEX, identity.getUserName());
		st.setString(FIRST_NAME_INDEX, firstName);
		st.setString(LAST_NAME_INDEX, lastName);
		st.setString(EMAIL_INDEX, email);
		st.setString(PASSWORD_INDEX, identity.getPassword());
		st.setString(PHONE_NUMBER_INDEX, phoneNumber);
		st.setString(STREET_INDEX, street);
		st.setString(CITY_INDEX, city);
		st.setString(COUNTRY_INDEX, country);
		return st;
	}

	public static ResponseData addNewUser(User user, Connection connection) {
		ResponseData rs = new ResponseData();
		try {
			user.registerUser(connection, USER_TABLE);
		} catch (SQLException e) {
			try {
				List<String> errors = new ArrayList<>();
				if (user.isAlreadyRegisteredUserName(connection)) {
					errors.add(DUPLICATE_USER_NAME_ERROR);
				} 
				if (user.isAlreadyRegisteredEmail(connection)) {
					errors.add(DUPLICATE_EMAIL_ERROR);
				}
				String combinedErrors = errors.stream().map(error -> "* " + error)
						.collect(Collectors.joining(System.lineSeparator()));
				rs.setError(combinedErrors);
			} catch (SQLException e1) {
				rs.setError("Please try again later");
			}
		}
		return rs;
	}

	private boolean isAlreadyRegisteredEmail(Connection connection) throws SQLException {
		return isAlreadyRegistered(connection, GET_USER_BY_EMAIL, email);
	}

	private boolean isAlreadyRegisteredUserName(Connection connection) throws SQLException {
		return isAlreadyRegistered(connection, GET_USER_BY_USER_NAME, identity.getUserName());
	}

	private boolean isAlreadyRegistered(Connection connection, String selectionType, String attribute)
			throws SQLException {
		String query = String.format(selectionType, USER_TABLE);
		PreparedStatement st = connection.prepareStatement(query);
		st.setString(1, attribute);
		ResultSet rs = st.executeQuery();
		return rs.next();
	}
}
