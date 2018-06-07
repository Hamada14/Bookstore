package server.database.entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Author extends Person {

	public static final int ERROR_AUTHOR_ADDITION = -1;
	private static final int AUTHOR_NOT_FOUND = -2;
	private static final String SELECT_WITH_COND = "SELECT ID FROM %s WHERE FIRST_NAME=?, LAST_NAME=?";
	private static final String ADD_AUTHOR = "INSERT INTO %s(FIRST_NAME, LAST_NAME) VALUES(?, ?)";
	private static final String AUTHOR_TABLE = "AUTHOR";
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
			int id = -1;
			while (rs.next()) {
				id = rs.getInt(ID_COL);
			}
			return id;
		} catch (SQLException e) {
			return AUTHOR_NOT_FOUND;
		}
	}
}
