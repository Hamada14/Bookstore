package server.database.entities;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import server.ResponseData;

public class Identity implements Serializable {

	private static final long serialVersionUID = -8050350344524286767L;

	private static final String GET_USER_BY_EMAIL_PASSWORD = "Select * from %s where USER_NAME = ? AND PASSWORD_SHA1 = SHA1(?);";
	private static final int USER_NAME_INDEX = 1;
	private static final int PASSWORD_INDEX = 2;
	
	private static final String INVALID_CREDENTIALS = "Invalid credentials specified.";

	private static final int IS_MANAGER_COL_INDEX = 8;

	private String userName;
	private String password;

	public Identity() {
	}

	public Identity(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}

	public ResponseData isValidIdentity(Connection connection) {
		ResponseData rd = new ResponseData();
		try {
			if (!isValidUser(connection)) {
				rd.setError(INVALID_CREDENTIALS);
			}
		} catch (SQLException e) {
			rd.setError("Unhandled SQL exception");
			e.printStackTrace();
		}
		return rd;
	}

	private boolean isValidUser(Connection connection) throws SQLException {
		ResultSet rs = getUser(connection);
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
