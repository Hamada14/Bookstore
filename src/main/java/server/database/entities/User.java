package server.database.entities;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import server.ResponseData;
import server.UserResponseData;

@Getter
@Setter
public class User implements Serializable {

	private static final long serialVersionUID = -6108052518548327564L;

	public static final String USER_TABLE = "CUSTOMER";

	private static final String NEW_USER_QUERY = "Insert into %s(USER_NAME, EMAIL, FIRST_NAME, LAST_NAME,  PASSWORD_SHA1, PHONE_NUMBER, STREET, CITY, COUNTRY)"
			+ "VALUES (?, ?, ?, ?, SHA1(?), ?, ?, ?, ?);";

	private static final String UPDATE_USER_INFO_QUERY = "Update %s SET  FIRST_NAME = ?, LAST_NAME = ?, PHONE_NUMBER = ?, STREET = ?, CITY = ?, COUNTRY = ?"
			+ "WHERE USER_NAME = ? AND PASSWORD_SHA1 = SHA1(?);";

	private static final String SELECT_USER_NAME = "SELECT * FROM %s WHERE USER_NAME = ?";

	private static final String INSERT_MANAGER = "INSERT INTO %s (USER_NAME) VALUES (?)";

	private static final String MANAGER_TABLE = "MANAGER";

	private static final int USER_NAME_INDEX = 1;
	private static final int EMAIL_INDEX = 2;
	private static final int FIRST_NAME_INDEX = 3;
	private static final int LAST_NAME_INDEX = 4;
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

	public User(ResultSet rs, Identity identity) throws SQLException {
		this.identity = identity;
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

	private PreparedStatement createUpdatePersonalInfoQuery(Connection con, String tableName) throws SQLException {

		String query = String.format(UPDATE_USER_INFO_QUERY, tableName);
		PreparedStatement st = con.prepareStatement(query);
		st.setString(FIRST_NAME_INDEX - 2, firstName);
		st.setString(LAST_NAME_INDEX - 2, lastName);
		st.setString(PHONE_NUMBER_INDEX - 3, phoneNumber);
		st.setString(STREET_INDEX - 3, street);
		st.setString(CITY_INDEX - 3, city);
		st.setString(COUNTRY_INDEX - 3, country);
		st.setString(USER_NAME_INDEX + 6, identity.getUserName());
		st.setString(PASSWORD_INDEX + 3, identity.getPassword());
		return st;
	}

	public static UserResponseData editPersonalInformation(User modifiedUser, Connection connection) {
		UserResponseData rs = new UserResponseData();
		try {
			PreparedStatement ps = modifiedUser.createUpdatePersonalInfoQuery(connection, USER_TABLE);
			ps.executeUpdate();
			rs.setUser(modifiedUser);
		} catch (SQLException e) {
			rs.setError(e.getMessage());
		}
		return rs;
	}

	public static List<String> getValidCountries() {
		Locale[] locales = Locale.getAvailableLocales();
		List<String> countries = new ArrayList<>();
		for (Locale locale : locales) {
			String country = locale.getDisplayCountry();
			if (country.trim().length() > 0 && !countries.contains(country)) {
				countries.add(country);
			}
		}
		Collections.sort(countries);
		return countries;
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

	public static boolean promoteUser(String userName, Connection connection) {
		boolean validUserName = isExistingUserName(userName, connection);
		if (!validUserName) {
			return false;
		}
		try {
			String query = String.format(INSERT_MANAGER, MANAGER_TABLE);
			PreparedStatement st = (PreparedStatement) connection.prepareStatement(query);
			st.setString(1, userName);
			int rowsAffected = st.executeUpdate();
			return rowsAffected == 1;
		} catch (SQLException e) {
			return false;
		}
	}

	private static boolean isExistingUserName(String userName, Connection connection) {
		try {
			String query = String.format(SELECT_USER_NAME, USER_TABLE);
			PreparedStatement st = (PreparedStatement) connection.prepareStatement(query);
			st.setString(1, userName);
			ResultSet rs = st.executeQuery();
			return rs != null && rs.next();
		} catch (SQLException e) {
			return false;
		}
	}
}
