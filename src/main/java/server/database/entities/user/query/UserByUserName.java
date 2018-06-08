package server.database.entities.user.query;

import java.sql.Connection;
import java.sql.SQLException;

import lombok.Setter;
import server.database.Query;
import server.database.entities.user.UserModel;

public class UserByUserName extends Query {

	private static final String GET_USER_BY_USER_NAME = "Select * from %s where USER_NAME = ?;";
	private static final int USER_NAME_INDEX = 1;
	
	@Setter private String userName;
	
	@Override
	protected void prepareStatement(Connection connection) throws SQLException {
		String query = String.format(GET_USER_BY_USER_NAME, UserModel.USER_TABLE);
		ps = connection.prepareStatement(query);
		ps.setString(USER_NAME_INDEX, userName);
	}
}
