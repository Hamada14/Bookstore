package server.database.entities.user;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import lombok.Getter;
import lombok.Setter;
import server.database.Query;
import server.database.entities.user.query.NewUser;
import server.database.entities.user.query.UpdateUser;
import server.database.entities.user.query.UserByEmail;
import server.database.entities.user.query.UserByUserName;

@Getter
@Setter
public class User implements Serializable {

	private static final long serialVersionUID = -6108052518548327564L;

	private static final int EMAIL_INDEX = 2;
	private static final int FIRST_NAME_INDEX = 3;
	private static final int LAST_NAME_INDEX = 4;
	private static final int PHONE_NUMBER_INDEX = 6;
	private static final int STREET_INDEX = 7;
	private static final int CITY_INDEX = 8;
	private static final int COUNTRY_INDEX = 9;

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

	User(String userName, String firstName, String lastName, String email, String password, String phoneNumber,
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

	void registerUser(Connection connection) throws SQLException {
		NewUser newUser = new NewUser();
		newUser.setUser(this);
		newUser.executeQuery(connection);
		newUser.close();
	}

	void updatePersonalInfo(Connection connection) throws SQLException {
		UpdateUser updateUser = new UpdateUser();
		updateUser.setUser(this);
		updateUser.executeQuery(connection);
		updateUser.close();
	}

	boolean isAlreadyRegisteredEmail(Connection connection) throws SQLException {
		UserByEmail userByEmail = new UserByEmail();
		userByEmail.setEmail(email);
		boolean result = isAlreadyRegistered(connection, userByEmail);
		userByEmail.close();
		return result;
	}

	boolean isAlreadyRegisteredUserName(Connection connection) throws SQLException {
		UserByUserName userByUserName = new UserByUserName();
		userByUserName.setUserName(identity.getUserName());
		boolean result = isAlreadyRegistered(connection, userByUserName);
		userByUserName.close();
		return result;
	}

	private boolean isAlreadyRegistered(Connection connection, Query query) throws SQLException {
		query.executeQuery(connection);
		ResultSet rs = query.getResultSet();
		return rs.next();
	}
}
