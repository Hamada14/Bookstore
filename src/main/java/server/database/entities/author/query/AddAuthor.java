package server.database.entities.author.query;

import java.sql.Connection;
import java.sql.SQLException;

import lombok.Setter;
import server.database.Query;
import server.database.entities.author.AuthorModel;

public class AddAuthor extends Query {
	private static final String ADD_AUTHOR = "INSERT INTO %s(FIRST_NAME, LAST_NAME) VALUES(?, ?)";

	private static final int FIRST_NAME_INDEX = 1;
	private static final int LAST_NAME_INDEX = 2;
	
	@Setter private String firstName;
	@Setter private String lastName;
	
	@Override
	protected void prepareStatement(Connection connection) throws SQLException {
		String query = String.format(ADD_AUTHOR, AuthorModel.AUTHOR_TABLE);
		ps = connection.prepareStatement(query);
		ps.setString(FIRST_NAME_INDEX, firstName);
		ps.setString(LAST_NAME_INDEX, lastName);
	}
}
