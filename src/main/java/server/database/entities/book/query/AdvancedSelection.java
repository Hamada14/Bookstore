package server.database.entities.book.query;

import java.sql.Connection;
import java.sql.SQLException;

import lombok.Getter;
import lombok.Setter;
import server.database.Query;
import server.database.entities.book.Author;
import server.database.entities.book.Book;
import server.database.entities.book.BookModel;
import server.database.entities.book.Publisher;

@Setter
@Getter
public class AdvancedSelection extends Query {
	private static final String SELECT_All = "SELECT  * FROM %s Where ISBN IN"
			+ " (SELECT BOOK_ISBN From %s JOIN %s ON AUTHOR_ID = ID" + " WHERE NAME LIKE ? "
			+ " AND PUBLISHER_ID IN (SELECT ID FROM %s WHERE NAME LIKE ?)"
			+ " AND CATEGORY IN (SELECT ID FROM %s WHERE CATEGORY LIKE ?)" + " AND TITLE LIKE ? AND ISBN LIKE ?";
	private static final int S_AUTHOR_INDEX = 1;
	private static final int S_PUBLISHER_INDEX = 2;
	private static final int S_CATEGORY_INDEX = 3;
	private static final int S_TITLE_INDEX = 4;
	private static final int S_ISBN_INDEX = 5;

	private Book book;
	private int offset;
	private int limit;

	@Override
	protected void prepareStatement(Connection connection) throws SQLException {
		boolean priceFound = false;
		boolean yearFound = false;
		String query = String.format(SELECT_All, BookModel.BOOK_TABLE, BookModel.BOOK_AUTHOR, Author.AUTHOR_TABLE,
				Publisher.PUBLISHER_TABLE, BookModel.BOOK_CATEGORY_TABLE);
		int index = S_ISBN_INDEX;
		if (!book.getPublicationYear().equals("")) {
			query += "AND PUBLICATION_YEAR = ?";
			yearFound = true;
		}

		if (book.getSellingPrice() != -1) {
			query += "AND SELLING_PRICE = ?";
			priceFound = true;
		}
		query += " LIMIT %d, %d;";
		query = String.format(query, offset, limit);
		ps = connection.prepareStatement(query);
		ps.setString(S_AUTHOR_INDEX, "%" + book.getAuthors().get(0).getName() + "%");
		ps.setString(S_CATEGORY_INDEX, "%" + book.getCategory() + "%");
		ps.setString(S_TITLE_INDEX, "%" + book.getBookTitle() + "%");
		ps.setString(S_PUBLISHER_INDEX, "%" + book.getPublisherName() + "%");
		ps.setString(S_ISBN_INDEX, "%" + book.getBookISBN() + "%");
		if (yearFound) {
			index++;
			ps.setInt(index, Integer.parseInt(book.getPublicationYear()));
		}
		if (priceFound) {
			index++;
			ps.setFloat(index, book.getSellingPrice());

		}
	}
}
