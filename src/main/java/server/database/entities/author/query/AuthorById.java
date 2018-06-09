package server.database.entities.author.query;

import java.sql.Connection;
import java.sql.SQLException;

import lombok.Setter;
import server.database.Query;
import server.database.entities.author.AuthorModel;

public class AuthorById extends Query {
	private static final String SELECT_WITH_ID = "SELECT FIRST_NAME, LAST_NAME FROM %s WHERE ID = ?";
	private static final int INDEX_ID = 1;
	
	@Setter private int id;

	@Override
	protected void prepareStatement(Connection connection) throws SQLException {
		String query = String.format(SELECT_WITH_ID, AuthorModel.AUTHOR_TABLE);
		ps = connection.prepareStatement(query);
		ps.setInt(INDEX_ID, id);
	}
}
