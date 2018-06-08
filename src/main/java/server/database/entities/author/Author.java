package server.database.entities.author;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import lombok.Getter;
import lombok.Setter;
import server.database.entities.Person;
import server.database.entities.author.query.AddAuthor;
import server.database.entities.author.query.AuthorByName;

@Getter
@Setter
public class Author extends Person {

	public static final int ERROR_AUTHOR_ADDITION = -1;
	public static final int AUTHOR_NOT_FOUND = -2;
	
	private String lastName;

	public Author(String firstName, String lastName) {
		super(firstName);
		this.lastName = lastName;
	}

	public Author() {
		super();
	}

	public boolean isValid() {
		return validateName(lastName) && validateName(name);
	}
	
	public int addAuthor(Connection connection) {
		if (!isValid()) {
			return ERROR_AUTHOR_ADDITION;
		}
		try {
			AddAuthor query = new AddAuthor();
			query.setFirstName(getName());
			query.setLastName(getLastName());
			query.executeQuery(connection);
			int rowsAffected = query.getUpdateCount();
			if (rowsAffected == 0) {
				throw new SQLException();
			}
			try (ResultSet generatedKeys = query.getGeneratedKeys()) {
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
	
	public int getID(Connection connection) {
		try {
			AuthorByName query = new AuthorByName();
			query.setFirstName(getName());
			query.setLastName(getLastName());
			query.executeQuery(connection);
			ResultSet rs = query.getResultSet();
			int id = Author.AUTHOR_NOT_FOUND;
			while (rs.next()) {
				id = rs.getInt(ID_COL);
			}
			return id;
		} catch (SQLException e) {
			return Author.AUTHOR_NOT_FOUND;
		}
	}
}
