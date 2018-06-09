package server.database.entities.book.query;

import java.sql.Connection;
import java.sql.SQLException;

import lombok.Setter;
import server.database.Query;
import server.database.entities.book.BookModel;

public class CategoryIDByName extends Query {
	private static final String SELECT_CATEGORY = "SELECT ID FROM %s WHERE CATEGORY=?";
	private static final int CATEGORY_INDEX = 1;
	
	@Setter private String category;

	@Override
	protected void prepareStatement(Connection connection) throws SQLException {
		String query = String.format(SELECT_CATEGORY, BookModel.BOOK_CATEGORY_TABLE);
		ps = connection.prepareStatement(query);
		ps.setString(CATEGORY_INDEX, category);
	}
}
