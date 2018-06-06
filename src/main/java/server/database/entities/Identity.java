package server.database.entities;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import lombok.Getter;
import lombok.Setter;
import server.ResponseData;

@Setter @Getter
public class Identity implements Serializable {

	private static final long serialVersionUID = -8050350344524286767L;

	private static final String MANAGER_TABLE = "MANAGER";
	
	private static final String GET_USER_BY_EMAIL_PASSWORD = "Select * from %s where USER_NAME = ? AND PASSWORD_SHA1 = SHA1(?);";
	private static final int USER_NAME_INDEX_IN_USER = 1;
	private static final int PASSWORD_INDEX_IN_USER = 2;
	
	private static final String GET_MANAGER_BY_USER_NAME = "Select * from %s where USER_NAME = ?;";
	private static final int USER_NAME_INDEX_IN_MANAGER = 1;
	
	private static final String INVALID_CREDENTIALS = "Invalid credentials specified.";

	private String userName;
	private String password;

	public Identity() {
	}

	public Identity(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}

	public ResponseData isUser(Connection connection) {
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


	public boolean isManager(Connection connection) {
		boolean isManager = false;
		try {
			isManager = isValidManager(connection);
		} catch (SQLException e) {
			return false;
		}
		return isManager;
	}
	
	private boolean isValidUser(Connection connection) throws SQLException {
		ResultSet rs = getUser(connection);
		return rs.next();
	}
	
	private boolean isValidManager(Connection connection) throws SQLException {
		String query = String.format(GET_MANAGER_BY_USER_NAME, Identity.MANAGER_TABLE);
		PreparedStatement st = connection.prepareStatement(query);
		st.setString(USER_NAME_INDEX_IN_MANAGER, userName);
		return st.executeQuery().next();
	}

	private ResultSet getUser(Connection connection) throws SQLException {
		String query = String.format(GET_USER_BY_EMAIL_PASSWORD, User.USER_TABLE);
		PreparedStatement st = connection.prepareStatement(query);
		st.setString(USER_NAME_INDEX_IN_USER, userName);
		st.setString(PASSWORD_INDEX_IN_USER, password);
		return st.executeQuery();
	}
}
