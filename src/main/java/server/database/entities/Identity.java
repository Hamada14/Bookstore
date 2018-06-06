package server.database.entities;

import java.io.Serializable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import server.UserResponseData;


public class Identity implements Serializable {

	private static final long serialVersionUID = -8050350344524286767L;

	private static final String GET_USER_BY_EMAIL_PASSWORD = "Select * from %s where USER_NAME = ? AND PASSWORD_SHA1 = SHA1(?);";
	private static final int USER_NAME_INDEX = 1;
	private static final int PASSWORD_INDEX = 2;
	
	private static final String INVALID_CREDENTIALS = "Invalid credentials specified.";
	private static final int USER_NAME_COL_INDEX = 1;
	private static final int EMAIL_COL_INDEX = 2;
	private static final int FISRT_NAME_COL_INDEX = 3;
	private static final int LAST_NAME_COL_INDEX = 4;
	private static final int PASSWORD_COL_INDEX = 5;
	private static final int PHONENUMBER_COL_INDEX = 6;	
	private static final int ADDRESS_COL_INDEX = 7;	
	private static final int IS_MANAGER_COL_INDEX = 8;

	private String userName;
	private String password;

	public Identity() {
	}

	public Identity(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}

	public UserResponseData isValidIdentity(Connection connection) {
		UserResponseData rd = new UserResponseData();
		try {
			ResultSet rs = getUser(connection);
			if (!isValidUser(rs)) {
				rd.setError(INVALID_CREDENTIALS);
			} else {
				constructUser(rs,rd);
			}
		} catch (SQLException e) {
			rd.setError("Unhandled SQL exception");
			e.printStackTrace();
		}
		return rd;
	}

	private boolean isValidUser(ResultSet rs) throws SQLException {
		return rs.next();
	}
		
	

	public boolean isManager(Connection connection) {
		ResultSet rs;
		boolean isManager = false;
		try {
			rs = getUser(connection);
			isManager = rs.getBoolean(IS_MANAGER_COL_INDEX);
		} catch (SQLException e) {
			return false;
		}
		return isManager;
	}

	private ResultSet getUser(Connection connection) throws SQLException {
		String query = String.format(GET_USER_BY_EMAIL_PASSWORD, User.USER_TABLE);
		PreparedStatement st = connection.prepareStatement(query);
		st.setString(USER_NAME_INDEX, userName);
		st.setString(PASSWORD_INDEX, password);
		return st.executeQuery();
	}

	private void constructUser(ResultSet rs, UserResponseData urs) {
		UserBuilder builder = new UserBuilder();
		try {
			builder.setUserName(rs.getString(USER_NAME_COL_INDEX));
			builder.setPassword(rs.getString(PASSWORD_COL_INDEX));
			builder.setFirstName(rs.getString(FISRT_NAME_COL_INDEX));
			builder.setLastName(rs.getString(LAST_NAME_COL_INDEX));
			builder.setAddress(rs.getString(ADDRESS_COL_INDEX));
			builder.setPhoneNumber(rs.getString(PHONENUMBER_COL_INDEX));
			builder.setEmail(rs.getString(EMAIL_COL_INDEX));
	
			urs.setUser(builder.buildUser());
			
		}catch (SQLException e) {
			
		   urs.setError(e.getMessage());
		}
		
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}
}
