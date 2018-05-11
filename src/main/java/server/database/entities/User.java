package server.database.entities;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6108052518548327564L;

	private static final String NEW_USER_QUERY = "Insert into %s(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD_SHA1, PHONE_NUMBER, ADDRESS)"
			+ "VALUES (%s, %s, %s, SHA1(%s), %s, %s);";

	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String address;
	private String phoneNumber;
	private boolean isManager;

	public User(String firstName, String lastName, String email, String password, String address, String phoneNumber,
			boolean isManager) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.isManager = isManager;
	}

	public User() {
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
		return null;
	}

	private String createInsertUserQuery(String tableName) {
		String query = String.format(NEW_USER_QUERY, tableName, addQuotes(firstName), addQuotes(lastName), addQuotes(email),
				addQuotes(password), addQuotes(phoneNumber), addQuotes(address));
		System.out.print(query);
		return query;
	}
	
	private String addQuotes(String str) {
		return "\"" + str + "\"";
	}
}
