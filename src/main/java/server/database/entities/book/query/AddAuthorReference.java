package server.database.entities.book.query;

import java.sql.Connection;
import java.sql.SQLException;

import lombok.Setter;
import server.database.Query;
import server.database.entities.book.BookModel;

public class AddAuthorReference extends Query {
	private static final String INSERT_AUTHOR_REF = "INSERT INTO %s (BOOK_ISBN, AUTHOR_ID) VALUES (?, ?)";
	private static final int ISBN_INDEX = 1;
	private static final int AUTHOR_ID_INDEX = 2;
	
	@Setter private String isbn;
	@Setter private int authorId;
	
	@Override
	protected void prepareStatement(Connection connection) throws SQLException {
		String query = String.format(INSERT_AUTHOR_REF, BookModel.BOOK_AUTHOR);
		ps = connection.prepareStatement(query);
		ps.setString(ISBN_INDEX, isbn);
		ps.setInt(AUTHOR_ID_INDEX, authorId);
	}
}
