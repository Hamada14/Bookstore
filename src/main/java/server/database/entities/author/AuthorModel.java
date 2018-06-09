package server.database.entities.author;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import server.database.entities.author.query.AuthorById;
import server.database.entities.author.query.AuthorByIsbn;

public class AuthorModel {

	public static final String AUTHOR_TABLE = "AUTHOR";
	private static final String AUTHOR_ID_COL = "AUTHOR_ID";
	private static final String NAME_COL = "NAME";
	private static final int AUTHOR_NOT_FOUND = -2;

	public static int addAuthor(Author author, Connection connection) {
		int authorId = author.getID(connection);
		if (authorId == Author.AUTHOR_NOT_FOUND) {
			authorId = author.addAuthor(connection);
			return authorId;
		} else {
			return authorId;
		}
	}
	
	public static int selectAuthorIdByISBN(String isbn, Connection connection) {
		try {
			AuthorByIsbn query = new AuthorByIsbn();
			query.setIsbn(isbn);
			query.executeQuery(connection);
			ResultSet rs = query.getResultSet();
			if(rs != null && rs.next()) {
				return rs.getInt(AUTHOR_ID_COL);
			}
			return AUTHOR_NOT_FOUND;
		} catch (SQLException e) {
			return AUTHOR_NOT_FOUND;
		}
	}
	
	public static Author selectAuthorNameByISBN(String isbn, Connection connection) {
		int id = selectAuthorIdByISBN(isbn, connection);
		if(id == AUTHOR_NOT_FOUND) {
			return new Author("");
		}
		return getAuthorById(id, connection);
	}
	
	public static Author getAuthorById(int id, Connection connection) {
		AuthorById query = new AuthorById();
		try {
			query.setId(id);
			query.executeQuery(connection);
			ResultSet rs = query.getResultSet();
			if(rs != null && rs.next()) {
				return new Author(rs.getString(NAME_COL));
			}
			return new Author("");
		} catch(SQLException e) {
			return new Author("");
		} finally {
			query.close();
		}
	}
}
