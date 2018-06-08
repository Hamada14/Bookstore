package server.database.entities.user.query;

import java.sql.Connection;
import java.sql.SQLException;

import lombok.Setter;
import server.database.Query;
import server.database.entities.user.UserModel;

public class UserByEmail extends Query {

	private static final String GET_USER_BY_EMAIL = "Select * from %s where EMAIL = ?;";
	private static final int EMAIL_INDEX = 1;
	
	@Setter private String email;
	
	@Override
	protected void prepareStatement(Connection connection) throws SQLException {
		String query = String.format(GET_USER_BY_EMAIL, UserModel.USER_TABLE);
		ps = connection.prepareStatement(query);
		ps.setString(EMAIL_INDEX, email);
	}
}
