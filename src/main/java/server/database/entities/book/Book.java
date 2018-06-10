package server.database.entities.book;

import java.io.Serializable;
import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import server.ResponseData;
import server.database.entities.author.Author;
import server.database.entities.book.query.AddBook;
import server.database.entities.book.query.EditBook;
import server.database.entities.publisher.Publisher;
import server.database.entities.publisher.PublisherModel;
import server.errors.BookError;
import server.errors.SqlError;

@Getter
@Setter
public class Book implements Serializable {


	private static final long serialVersionUID = 7418014002918381057L;
	
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
	private String publisherName;
	private List<Author> authors;

	public Book() {
	}
	
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

	public Book(String bookISBN, String bookTitle, String publicationYear, float sellingPrice, String category,
			String publisherName,int quantity, int minimumThreshold) {
		authors = new ArrayList<Author>();
		authors.add(new Author(""));
		this.bookISBN = bookISBN;
		this.bookTitle = bookTitle;
		this.publicationYear = publicationYear;
		this.sellingPrice = sellingPrice;
		this.category = category;
		this.publisher = new Publisher(publisherName);
		this.quantity = quantity;
		this.minimumThreshold = minimumThreshold;	
	}
	public Book(String title) {
		this.bookTitle = title;
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

	public ResponseData bookAddition(Connection connection) {
		boolean isBookExisting = BookModel.selectBookByISBN(bookISBN, connection);
		ResponseData response = new ResponseData();
		if (isBookExisting) {
			response.setError(BookError.INVALID_BOOK_ISBN.toString());
			return response;
		}
		int categoryId = BookModel.getCategoryId(category, connection);
		if (categoryId == BookModel.CATEGORY_NOT_FOUND) {
			response.setError(BookError.INVALID_BOOK_CATEGOTY.toString());
			return response;
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
			return response;
		} catch (SQLException | NumberFormatException e) {
			response.setError(SqlError.DUPLICATE_KEY.toString());
			return response;
		} finally {
			query.close();
		}
	}

	public ResponseData editBook(Connection connection) {
		boolean isBookExisting = BookModel.selectBookByISBN(bookISBN, connection);
		ResponseData response = new ResponseData();
		if (!isBookExisting) {
			response.setError(BookError.BOOK_NOT_EXIST.toString());
			return response;
		}
		int categoryId = BookModel.getCategoryId(category, connection);
		if (categoryId == BookModel.CATEGORY_NOT_FOUND) {
			response.setError(BookError.INVALID_BOOK_CATEGOTY.toString());
			return response;
		}
		int publisherId = PublisherModel.addPublisher(publisher, connection);
		EditBook query = new EditBook();
		try {
			query.setIsbn(bookISBN);
			query.setTitle(bookTitle);
			query.setPublisherId(publisherId);
			query.setPublicationYear(publicationYear);
			query.setSellingPrice(sellingPrice);
			query.setCategoryId(categoryId);
			query.setQuantity(quantity);
			query.setMinThreshold(minimumThreshold);
			query.executeQuery(connection);
			return response;
		} catch (SQLException | NumberFormatException e) {
			response.setError(SqlError.DUPLICATE_KEY.toString());
			return response;
		} finally {
			query.close();
		}
	}
}

