package server.database.entities.book.query;

import java.sql.Connection;
import java.sql.SQLException;

import lombok.Setter;
import server.database.Query;
import server.database.entities.book.BookModel;

public class AddBook extends Query {

	private static final String INSERT_BOOK = "INSERT INTO %s(ISBN, TITLE, PUBLISHER_ID, PUBLICATION_YEAR,"
			+ " SELLING_PRICE, CATEGORY, MIN_THRESHOLD, QUANTITY)" + " VALUES(?,?,?,?,?,?,?,?);";

	private static final int ISBN_INDEX = 1;
	private static final int BOOK_TITLE_INDEX = 2;
	private static final int PUBLISHER_ID_INDEX = 3;
	private static final int PUBLICATION_YEAR_INDEX = 4;
	private static final int SELLING_PRICE_INDEX = 5;
	private static final int CATEGORY_INDEX = 6;
	private static final int MIN_THRESHOLD_INDEX = 7;
	private static final int QUANTITY_INDEX = 8;

	@Setter private String isbn;
	@Setter private String title;
	@Setter private int categoryID;
	@Setter private int publisherID;
	@Setter private String publicationYear;
	@Setter private int quantity;
	@Setter private int minThreshold;
	@Setter private float sellingPrice;

	@Override
	protected void prepareStatement(Connection connection) throws SQLException {
		String query = String.format(INSERT_BOOK, BookModel.BOOK_TABLE);
		ps = connection.prepareStatement(query);
		ps.setString(ISBN_INDEX, isbn);
		ps.setString(BOOK_TITLE_INDEX, title);
		ps.setInt(CATEGORY_INDEX, categoryID);
		ps.setInt(PUBLISHER_ID_INDEX, publisherID);
		ps.setString(PUBLICATION_YEAR_INDEX, publicationYear);
		ps.setFloat(SELLING_PRICE_INDEX, sellingPrice);
		ps.setInt(MIN_THRESHOLD_INDEX, minThreshold);
		ps.setInt(QUANTITY_INDEX, quantity);
	}
}
