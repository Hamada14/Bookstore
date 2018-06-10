package server.database.entities.author.query;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import lombok.Setter;
import server.database.Query;
import server.database.entities.author.AuthorModel;

public class AddAuthor extends Query {
	private static final String ADD_AUTHOR = "INSERT INTO %s(NAME) VALUES(?)";

	private static final int NAME_INDEX = 1;
	
	@Setter private String name;
	
	@Override
	protected void prepareStatement(Connection connection) throws SQLException {
		String query = String.format(ADD_AUTHOR, AuthorModel.AUTHOR_TABLE);
		ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		ps.setString(NAME_INDEX, name);
	}
}
