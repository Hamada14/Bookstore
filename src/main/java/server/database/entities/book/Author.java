package server.database.entities.book;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Getter;
import lombok.Setter;
import server.database.entities.Person;

@Getter
@Setter
public class Author extends Person {

	public static final int ERROR_AUTHOR_ADDITION = -1;
	private static final int AUTHOR_NOT_FOUND = -2;
	private static final String SELECT_WITH_COND = "SELECT ID FROM %s WHERE FIRST_NAME=? AND LAST_NAME=?";
	private static final String SELECT_WITH_ID = "SELECT FIRST_NAME, LAST_NAME FROM %s WHERE ID = ?";
	private static final String SELECT_ID_BY_ISBN = "SELECT AUTHOR_ID FROM %s WHERE BOOK_ISBN = ?";
	private static final String ADD_AUTHOR = "INSERT INTO %s(FIRST_NAME, LAST_NAME) VALUES(?, ?)";
	private static final String AUTHOR_TABLE = "AUTHOR";
	private static final String BOOK_AUTHORS_TABLE = "BOOK_AUTHOR";
	private static final String AUTHOR_ID_COL = "AUTHOR_ID";
	private static final String FIRST_NAME_COL = "FIRST_NAME";
	private static final String LAST_NAME_COL = "LAST_NAME";
	private static final int AUTHOR_FIRST_NAME = 1;
	private static final int AUTHOR_LAST_NAME = 2;

	private String LastName;

	public Author(String firstName, String lastName) {
		super(firstName);
		this.LastName = lastName;
	}
	
	public Author() {
		super();
	}

	public static int addAuthor(Author author, Connection connection) {
		int authorId = selectAuthor(author, connection);
		if (authorId == AUTHOR_NOT_FOUND) {
			authorId = authorAddition(author, connection);
			return authorId;
		} else {
			return authorId;
		}
	}

	private static int authorAddition(Author author, Connection connection) {
		boolean validNames = validateName(author.getName()) && validateName(author.getLastName());
		if (!validNames) {
			return ERROR_AUTHOR_ADDITION;
		}
		try {
			String query = String.format(ADD_AUTHOR, AUTHOR_TABLE);
			PreparedStatement st = (PreparedStatement) connection.prepareStatement(query,
					Statement.RETURN_GENERATED_KEYS);
			st.setString(AUTHOR_FIRST_NAME, author.getName());
			st.setString(AUTHOR_LAST_NAME, author.getLastName());
			int rowsAffected = st.executeUpdate();
			if(rowsAffected == 0) {
				throw new SQLException();
			}
			try (ResultSet generatedKeys = st.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					return generatedKeys.getInt(1);
				} else {
					throw new SQLException();
				}
			}
		} catch (SQLException e) {
			return ERROR_AUTHOR_ADDITION;
		}
	}

	private static int selectAuthor(Author author, Connection connection) {
		try {
			String query = String.format(SELECT_WITH_COND, AUTHOR_TABLE);
			PreparedStatement st = (PreparedStatement) connection.prepareStatement(query);
			st.setString(AUTHOR_FIRST_NAME, author.getName());
			st.setString(AUTHOR_LAST_NAME, author.getLastName());
			ResultSet rs = st.executeQuery();
			int id = AUTHOR_NOT_FOUND;
			while (rs.next()) {
				id = rs.getInt(ID_COL);
			}
			return id;
		} catch (SQLException e) {
			return AUTHOR_NOT_FOUND;
		}
	}
	
	public static int selectAuthorIdByISBN(String isbn, Connection connection) {
		try {
			String query = String.format(SELECT_ID_BY_ISBN, BOOK_AUTHORS_TABLE);
			PreparedStatement st = (PreparedStatement) connection.prepareStatement(query);
			st.setString(1, isbn);
			ResultSet rs = st.executeQuery();
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
			return new Author("", "");
		}
		try {
			String query = String.format(SELECT_WITH_ID, AUTHOR_TABLE);
			PreparedStatement st = (PreparedStatement) connection.prepareStatement(query);
			st.setInt(1, id);
			ResultSet rs = st.executeQuery();
			if(rs != null && rs.next()) {
				return new Author(rs.getString(FIRST_NAME_COL), rs.getString(LAST_NAME_COL));
			}
			return new Author("", "");
		} catch(SQLException e) {
			return new Author("", "");
		}
	}
}
