package server.database.entities.book.query;

import java.sql.Connection;
import java.sql.SQLException;

import server.database.Query;
import server.database.entities.book.BookModel;

public class AllBooks extends Query {
	private static final String SELECT_All = "SELECT  * FROM %s";

	@Override
	protected void prepareStatement(Connection connection) throws SQLException {
		String query = String.format(SELECT_All, BookModel.BOOK_TABLE);
		ps = connection.prepareStatement(query);
	}
}
