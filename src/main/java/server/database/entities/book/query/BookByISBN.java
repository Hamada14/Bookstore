package server.database.entities.book.query;

import java.sql.Connection;
import java.sql.SQLException;

import lombok.Setter;
import server.database.Query;
import server.database.entities.book.BookModel;

public class BookByISBN extends Query {

	private static final String SELECT_BOOK = "SELECT * FROM %s where ISBN = ?";
	private static final int ISBN_INDEX = 1;
	
	@Setter private String isbn;
	
	@Override
	protected void prepareStatement(Connection connection) throws SQLException {
		String query = String.format(SELECT_BOOK, BookModel.BOOK_TABLE);
		ps = connection.prepareStatement(query);
		ps.setString(ISBN_INDEX, isbn);
	}
}
