package server.database.entities.book.query;

import java.sql.Connection;
import java.sql.SQLException;

import lombok.Setter;
import server.database.Query;
import server.database.entities.book.BookModel;

public class CategoryById extends Query {
	private static final String SELECT_BY_ID = "select * from %s where ID = ?;";
	private static final int ID_INDEX = 1;
	
	@Setter private int id;
	
	@Override
	protected void prepareStatement(Connection connection) throws SQLException {
		String query = String.format(SELECT_BY_ID, BookModel.BOOK_CATEGORY_TABLE);
		ps = connection.prepareStatement(query);
		ps.setInt(ID_INDEX, id);
	}
}
