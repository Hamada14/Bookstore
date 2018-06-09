package server.database.entities.book.query;

import java.sql.Connection;
import java.sql.SQLException;

import server.database.Query;
import server.database.entities.book.BookModel;

public class AllCategories extends Query {
	private static final String SELECT_ALL = "Select * from BOOK_CATEGORY;";

	@Override
	protected void prepareStatement(Connection connection) throws SQLException {
		String query = String.format(SELECT_ALL, BookModel.BOOK_CATEGORY_TABLE);
		ps = connection.prepareStatement(query);
	}
}
