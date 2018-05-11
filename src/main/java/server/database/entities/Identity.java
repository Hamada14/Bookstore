package server.database.entities;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import server.ResponseData;

public class Identity extends Entity implements Serializable {

	private static final long serialVersionUID = -8050350344524286767L;

	private static final String GET_USER_BY_EMAIL_PASSWORD = "Select * from %s where EMAIL = %s AND PASSWORD_SHA1 = SHA1(%s);";

	private static final String INVALID_CREDENTIALS = "Invalid credentials specified.";

	private static final int IS_MANAGER_COL_INDEX = 7;
	
	private String email;
	private String password;
	
	public Identity() {
	}
	
	public Identity(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public ResponseData isValidIdentity(Connection connection) {
		ResponseData rd = new ResponseData();
		try {
			ResultSet rs = getUser(connection);
			if (!rs.next()) {
				rd.setError(INVALID_CREDENTIALS);
			}
		} catch (SQLException e) {
			rd.setError("Unhandled SQL exception");
			e.printStackTrace();
		}
		return rd;
	}

	public boolean isManager(Connection connection) {
		ResultSet rs;
		boolean isManager = false;
		try {
			rs = getUser(connection);
			isManager =  rs.getBoolean(IS_MANAGER_COL_INDEX);
		} catch (SQLException e) {
			return false;
		}
		return isManager;
	}
	
	private ResultSet getUser(Connection connection) throws SQLException {
		Statement st = connection.createStatement();
		String query = String.format(GET_USER_BY_EMAIL_PASSWORD, User.USER_TABLE, addQuotes(email), addQuotes(password));
		return st.executeQuery(query);
	}
}
