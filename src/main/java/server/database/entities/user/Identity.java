package server.database.entities.user;

import java.io.Serializable;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import server.ResponseData;
import server.UserResponseData;
import server.database.entities.user.query.ManagerByUserName;
import server.database.entities.user.query.UpdatePassword;
import server.database.entities.user.query.UserByUserNameAndPassword;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class Identity implements Serializable {

	private static final long serialVersionUID = -8050350344524286767L;
	
	private static final String INVALID_CREDENTIALS = "Invalid credentials specified.";

	private String userName;
	private String password;

	public Identity() {
	}

	public Identity(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}

	public UserResponseData isUser(Connection connection) {
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
		try {
			UpdatePassword query = new UpdatePassword();
			query.setUserName(userName);
			query.setPassword(password);
			query.setNewPassword(newPassword);
			query.executeQuery(connection);
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
		ManagerByUserName query = new ManagerByUserName();
		query.setUserName(userName);
		query.executeQuery(connection);
		return query.getResultSet().next();
	}

	private ResultSet getUser(Connection connection) throws SQLException {
		UserByUserNameAndPassword query = new UserByUserNameAndPassword();
		query.setUserName(userName);
		query.setPassword(password);
		query.executeQuery(connection);
		return query.getResultSet();
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
