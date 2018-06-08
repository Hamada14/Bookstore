package server.database.entities.user.query;

import java.sql.Connection;
import java.sql.SQLException;

import lombok.Setter;
import server.database.Query;
import server.database.entities.user.UserModel;

public class PromoteUser extends Query {

	private static final String INSERT_MANAGER = "INSERT INTO %s (USER_NAME) VALUES (?)";

	private static final int USER_NAME_INDEX = 1;
	
	@Setter private String userName;
	
	@Override
	protected void prepareStatement(Connection connection) throws SQLException {
		String query = String.format(INSERT_MANAGER, UserModel.MANAGER_TABLE);
		ps = connection.prepareStatement(query);
		ps.setString(USER_NAME_INDEX, userName);
	}
}
