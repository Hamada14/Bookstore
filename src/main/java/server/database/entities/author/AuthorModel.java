package server.database.entities.author;

import java.sql.Connection;

public class AuthorModel {

	public static final String AUTHOR_TABLE = "AUTHOR";
	
	public static int addAuthor(Author author, Connection connection) {
		int authorId = author.getID(connection);
		if (authorId == Author.AUTHOR_NOT_FOUND) {
			authorId = author.addAuthor(connection);
			return authorId;
		} else {
			return authorId;
		}
	}
}
