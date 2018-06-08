package server.database.entities;

import java.io.Serializable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import server.ResponseData;
import server.UserResponseData;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class Identity implements Serializable {

	private static final long serialVersionUID = -8050350344524286767L;

	private static final String MANAGER_TABLE = "MANAGER";
	
	private static final String GET_USER_BY_EMAIL_PASSWORD = "Select * from %s where USER_NAME = ? AND PASSWORD_SHA1 = SHA1(?);";
	private static final int USER_NAME_INDEX_IN_USER = 1;
	private static final int PASSWORD_INDEX_IN_USER = 2;
	
	private static final String UPDATE_PASSWORD = "UPDATE %s SET PASSWORD_SHA1 = SHA1(?) where USER_NAME = ? AND PASSWORD_SHA1 = SHA1(?);";
	private static final int NEWPASSWORD_INDEX_IN_UPDATE = 1;
	private static final int USER_NAME_INDEX_IN_UPDATE = 2;
	private static final int PASSWORD_INDEX_IN_UPDATE  = 3;
	
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
	

	
	
	public  ResponseData editUserIdentity( String newPassword, Connection connection) {
		ResponseData rs = new ResponseData();
		String query = String.format(UPDATE_PASSWORD, User.USER_TABLE);
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(query);
			ps.setString(NEWPASSWORD_INDEX_IN_UPDATE, newPassword);
			ps.setString(USER_NAME_INDEX_IN_UPDATE, this.userName);
			ps.setString(PASSWORD_INDEX_IN_UPDATE, this.password);
			ps.executeUpdate();
		} catch (SQLException e) {
			rs.setError(e.getMessage());
			e.printStackTrace();
		}
		return rs;
	}

	
	private boolean isValidUser(ResultSet rs) throws SQLException {
		return rs.next();
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

	private void constructUser(ResultSet rs, UserResponseData urs) {		
		try {
			User currentUser = new User(rs, this);
			urs.setUser(currentUser);
		}catch (SQLException e) {
		   urs.setError(e.getMessage());
		}	
	}
	
	
	
}
