package server.database.entities.user.query;

import java.sql.Connection;
import java.sql.SQLException;

import lombok.Setter;
import server.database.Query;
import server.database.entities.user.UserModel;

public class UpdatePassword extends Query {

	private static final String UPDATE_PASSWORD = "UPDATE %s SET PASSWORD_SHA1 = SHA1(?) where USER_NAME = ? AND PASSWORD_SHA1 = SHA1(?);";
	
	private static final int NEW_PASSWORD_INDEX = 1;
	private static final int USER_NAME_INDEX = 2;
	private static final int PASSWORD_INDEX  = 3;
	
	@Setter private String userName;
	@Setter private String password;
	@Setter private String newPassword;
	
	@Override
	protected void prepareStatement(Connection connection) throws SQLException {
		String query = String.format(UPDATE_PASSWORD, UserModel.USER_TABLE);
		ps = connection.prepareStatement(query);
		ps.setString(NEW_PASSWORD_INDEX, newPassword);
		ps.setString(USER_NAME_INDEX, userName);
		ps.setString(PASSWORD_INDEX, password);
	}
}
