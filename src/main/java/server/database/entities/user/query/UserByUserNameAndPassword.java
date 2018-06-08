package server.database.entities.user.query;

import java.sql.Connection;
import java.sql.SQLException;

import lombok.Setter;
import server.database.Query;
import server.database.entities.user.UserModel;

public class UserByUserNameAndPassword extends Query {

	private static final String GET_USER_BY_USER_NAME_PASSWORD = "Select * from %s where USER_NAME = ? AND PASSWORD_SHA1 = SHA1(?);";
	private static final int USER_NAME_INDEX = 1;
	private static final int PASSWORD_INDEX = 2;
	
	@Setter private String userName;
	@Setter private String password;
	
	@Override
	protected void prepareStatement(Connection connection) throws SQLException {
		String query = String.format(GET_USER_BY_USER_NAME_PASSWORD, UserModel.USER_TABLE);
		ps = connection.prepareStatement(query);
		ps.setString(USER_NAME_INDEX, userName);
		ps.setString(PASSWORD_INDEX, password);
	}
}
