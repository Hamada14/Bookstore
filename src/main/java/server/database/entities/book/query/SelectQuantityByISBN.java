package server.database.entities.book.query;

import java.sql.Connection;
import java.sql.SQLException;

import lombok.Getter;
import lombok.Setter;
import server.database.Query;

@Setter
@Getter
public class SelectQuantityByISBN extends Query{
	private static final String SELECT_QUANTITY = "SELECT QUANTITY from %s WHERE ISBN = ?;";
	private static final String BOOKS_TABLE = "BOOK";
	private static final int ISBN_INDEX = 1;
	private String isbn;
	
	@Override
	protected void prepareStatement(Connection connection) throws SQLException {
		String query = String.format(SELECT_QUANTITY, BOOKS_TABLE);
		ps = connection.prepareStatement(query);
		ps.setString(ISBN_INDEX, isbn);
		
		
	}

}
