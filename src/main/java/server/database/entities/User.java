package server.database.entities;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import lombok.Getter;
import lombok.Setter;
import server.ResponseData;

@Getter @Setter
public class User implements Serializable {

	private static final long serialVersionUID = -6108052518548327564L;

	public static final String USER_TABLE = "CUSTOMER";

	private static final String NEW_USER_QUERY = "Insert into %s(USER_NAME, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD_SHA1, PHONE_NUMBER, STREET, CITY, COUNTRY)"
			+ "VALUES (?, ?, ?, ?, SHA1(?), ?, ?, ?, ?);";
	private static final int INSERT_USER_NAME_INDEX = 1;
	private static final int INSERT_FIRST_NAME_INDEX = 2;
	private static final int INSERT_LAST_NAME_INDEX = 3;
	private static final int INSERT_EMAIL_INDEX = 4;
	private static final int INSERT_PASSWORD_INDEX = 5;
	private static final int INSERT_PHONE_NUMBER_INDEX = 6;
	private static final int INSERT_STREET_INDEX = 7;
	private static final int INSERT_CITY_INDEX = 8;
	private static final int INSERT_COUNTRY_INDEX = 9;
	
	private static final String GET_USER_BY_EMAIL = "Select * from %s where EMAIL = ?;";
	private static final String GET_USER_BY_USER_NAME = "Select * from %s where USER_NAME = ?;";

	private static final String DUPLICATE_EMAIL_ERROR = "User with this email already registered";
	private static final String DUPLICATE_USER_NAME_ERROR = "User with this user name already registered";
	
	private String userName;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String phoneNumber;

	private String street;
	private String city;
	private String country;

	public User() {

	}

	public User(String userName, String firstName, String lastName, String email, String password, String phoneNumber,
			String street, String city, String country) {
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.street = street;
		this.city = city;
		this.country = country;
	}

	@Override
	public String toString() {
		return "User [firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + ", password=" + password
				+ ", phoneNumber=" + phoneNumber + "]";
	}

	public void registerUser(Connection connection, String tableName) throws SQLException {
		PreparedStatement registerUserQuery = createInsertUserQuery(connection, tableName);
		registerUserQuery.execute();
	}
	
	private PreparedStatement createInsertUserQuery(Connection con, String tableName) throws SQLException {
		String query = String.format(NEW_USER_QUERY, tableName);
		PreparedStatement st = con.prepareStatement(query);
		st.setString(INSERT_USER_NAME_INDEX, userName);
		st.setString(INSERT_FIRST_NAME_INDEX, firstName);
		st.setString(INSERT_LAST_NAME_INDEX, lastName);
		st.setString(INSERT_EMAIL_INDEX, email);
		st.setString(INSERT_PASSWORD_INDEX, password);
		st.setString(INSERT_PHONE_NUMBER_INDEX, phoneNumber);
		st.setString(INSERT_STREET_INDEX, street);
		st.setString(INSERT_CITY_INDEX, city);
		st.setString(INSERT_COUNTRY_INDEX, country);
		return st;
	}

	public static ResponseData addNewUser(User user, Connection connection) {
		ResponseData rs = new ResponseData();
		try {
			if (user.isAlreadyRegisteredUserName(connection)) {
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
		String query = String.format(selectionType, USER_TABLE);
		PreparedStatement st = connection.prepareStatement(query);
		st.setString(1, attribute);
		ResultSet rs = st.executeQuery();
		return rs.next();
	}
}
