package server.database.entities.author.query;

import java.sql.Connection;
import java.sql.SQLException;

import lombok.Setter;
import server.database.Query;
import server.database.entities.book.BookModel;

public class AuthorByIsbn extends Query {

	private static final String SELECT_ID_BY_ISBN = "SELECT AUTHOR_ID FROM %s WHERE BOOK_ISBN = ?";
	private static final int ISBN_INDEX = 1;
	
	@Setter private String isbn;
	
	@Override
	protected void prepareStatement(Connection connection) throws SQLException {
		String query = String.format(SELECT_ID_BY_ISBN, BookModel.BOOK_AUTHOR);
		ps = connection.prepareStatement(query);
		ps.setString(ISBN_INDEX, isbn);
	}
}
