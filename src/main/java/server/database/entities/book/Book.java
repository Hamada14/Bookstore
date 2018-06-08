package server.database.entities.book;

import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;

import lombok.Getter;
import lombok.Setter;
import server.database.entities.book.query.AddBook;
import server.database.entities.publisher.Publisher;
import server.database.entities.publisher.PublisherModel;

@Getter
@Setter
public class Book {

	private static final int ISBN_INDEX = 1;
	private static final int BOOK_TITLE_INDEX = 2;
	private static final int PUBLISHER_ID_INDEX = 3;
	private static final int PUBLICATION_YEAR_INDEX = 4;
	private static final int SELLING_PRICE_INDEX = 5;
	private static final int CATEGORY_INDEX = 6;
	private static final int MIN_THRESHOLD_INDEX = 7;
	private static final int QUANTITY_INDEX = 8;
	
	private String bookISBN;
	private String bookTitle;
	private String publicationYear;
	private float sellingPrice;
	private String category;
	private Publisher publisher;
	private int quantity;
	private int minimumThreshold;

	Book(String bookISBN, String bookTitle, String publicationYear, float sellingPrice, String category,
			Publisher publisher, int quantity, int minimumThreshold) {
		this.bookISBN = bookISBN;
		this.bookTitle = bookTitle;
		this.publicationYear = publicationYear;
		this.sellingPrice = sellingPrice;
		this.category = category;
		this.publisher = publisher;
		this.quantity = quantity;
		this.minimumThreshold = minimumThreshold;
	}

	public Book(ResultSet rs, Connection connection) throws SQLException {
		this.bookISBN = rs.getString(ISBN_INDEX);
		this.bookTitle = rs.getString(BOOK_TITLE_INDEX);
		this.publicationYear = Integer.toString(rs.getInt(PUBLICATION_YEAR_INDEX));
		this.sellingPrice = rs.getFloat(SELLING_PRICE_INDEX);
		this.category = BookModel.getCategory(rs.getInt(CATEGORY_INDEX), connection);
		this.quantity = rs.getInt(QUANTITY_INDEX);
		this.publisher = PublisherModel.getPublisherByID(rs.getInt(PUBLISHER_ID_INDEX), connection);
		this.minimumThreshold = rs.getInt(MIN_THRESHOLD_INDEX);

	}

	public boolean bookAddition(int authorId, Connection connection) {
		boolean isBookExisting = BookModel.selectBookByISBN(bookISBN, connection);
		if (isBookExisting) {
			return false;
		}
		int categoryId = BookModel.getCategoryId(category, connection);
		if (categoryId == BookModel.CATEGORY_NOT_FOUND) {
			return false;
		}
		int publisherId = PublisherModel.addPublisher(publisher, connection);
		AddBook query = new AddBook();
		try {
			query.setIsbn(bookISBN);
			query.setTitle(bookTitle);
			query.setPublisherID(publisherId);
			query.setPublicationYear(publicationYear);
			query.setSellingPrice(sellingPrice);
			query.setCategoryID(categoryId);
			query.setQuantity(quantity);
			query.setMinThreshold(minimumThreshold);
			query.executeQuery(connection);
			int rowsAffected = query.getUpdateCount();
			return rowsAffected == 1;
		} catch (SQLException | NumberFormatException e) {
			return false;
		} finally {
			query.close();
		}
	}
}
