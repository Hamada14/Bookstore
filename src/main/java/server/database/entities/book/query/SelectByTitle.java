package server.database.entities.book.query;

import java.sql.Connection;
import java.sql.SQLException;

import lombok.Getter;
import lombok.Setter;
import server.database.Query;
import server.database.entities.book.BookModel;
@Getter
@Setter
public class SelectByTitle extends Query{

	private static final String SELECT_BY_TITLE = "SELECT * FROM %s WHERE TITLE LIKE ? LIMIT %d, %d;";
	private String title;
	int offset;
	int limit;
	@Override
	protected void prepareStatement(Connection connection) throws SQLException {
		String query = String.format(SELECT_BY_TITLE, BookModel.BOOK_TABLE, offset, limit);
		ps = connection.prepareStatement(query);
		ps.setString(1, "%" + title + "%");
		System.out.println(ps.toString());
	}
}
