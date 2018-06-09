package server.database.entities.book.query;

import java.sql.Connection;
import java.sql.SQLException;

import lombok.Setter;
import server.database.Query;
import server.database.entities.book.BookModel;

public class EditBook extends Query {
	private static final String EDIT_BOOK = "UPDATE %s SET TITLE = ?, PUBLISHER_ID = ?, PUBLICATION_YEAR = ?," +
			"SELLING_PRICE = ?, CATEGORY = ?, MIN_THRESHOLD = ?, QUANTITY = ? WHERE ISBN = ?;";
	private static final int TITLE_INDEX = 1;
	private static final int PUBLISHER_ID_INDEX = 2;
	private static final int PUBLICATION_YEAR_INDEX = 3;
	private static final int SELLING_PRICE_INDEX = 4;
	private static final int CATEGORY_ID_INDEX = 5;
	private static final int MIN_THRESHOLD_INDEX = 6;
	private static final int QUANTITY_INDEX = 7;
	private static final int ISBN_INDEX = 8;
	
	@Setter private String title;
	@Setter private int publisherId;
	@Setter private String publicationYear;
	@Setter private float sellingPrice;
	@Setter private int categoryId;
	@Setter private int minThreshold;
	@Setter private int quantity;
	@Setter private String isbn;
	
					
	@Override
	protected void prepareStatement(Connection connection) throws SQLException {
		String query = String.format(EDIT_BOOK, BookModel.BOOK_TABLE);
		ps = connection.prepareStatement(query);
		ps.setString(TITLE_INDEX, title);
		ps.setInt(PUBLISHER_ID_INDEX, publisherId);
		ps.setString(PUBLICATION_YEAR_INDEX, publicationYear);
		ps.setFloat(SELLING_PRICE_INDEX, sellingPrice);
		ps.setInt(CATEGORY_ID_INDEX, categoryId);
		ps.setInt(MIN_THRESHOLD_INDEX, minThreshold);
		ps.setInt(QUANTITY_INDEX, quantity);
		ps.setString(ISBN_INDEX, isbn);
	}
}
