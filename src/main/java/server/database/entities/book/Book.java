package server.database.entities.book;

import java.io.Serializable;
import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Getter;
import lombok.Setter;
import server.ResponseData;
import server.database.entities.author.Author;
import server.database.entities.book.query.AddBook;
import server.database.entities.publisher.Publisher;
import server.database.entities.publisher.PublisherModel;
import server.errors.BookError;
import server.errors.SqlError;

@Getter
@Setter
public class Book implements Serializable {


	private static final long serialVersionUID = 7418014002918381057L;

//	private static final String SELECT_All = "SELECT  * FROM %s Where ISBN IN"
//			+ " (SELECT BOOK_ISBN From %s JOIN %s ON AUTHOR_ID = ID"
//			+ " WHERE FIRST_NAME LIKE ? AND LAST_NAME LIKE ?) "
//			+ " AND PUBLISHER_ID IN (SELECT ID FROM %s WHERE NAME LIKE ?)"
//			+ " AND CATEGORY IN (SELECT ID FROM %s WHERE CATEGORY LIKE ?)"
//			+ " AND TITLE LIKE ? AND ISBN LIKE ?";
//	private static final int S_AUTHORFN_INDEX = 1;
//	private static final int S_AUTHORLN_INDEX = 2;
//	private static final int S_PUBLISHER_INDEX = 3;
//	private static final int S_CATEGORY_INDEX = 4;
//	private static final int S_TITLE_INDEX = 5;
//	private static final int S_ISBN_INDEX = 6;


//	private static final String SELECT_CATEGORY = "SELECT ID FROM %s WHERE CATEGORY=?";
//	private static final String BOOK_CATEGORY_TABLE = "BOOK_CATEGORY";
//	private static final String BOOK_AUTHOR = "BOOK_AUTHOR";
//	private static final String BOOK_TABLE = "BOOK";
//	private static final String ID_COL = "ID";

	
	
	
	
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
	private Author author;

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

//	public static BooksResponseData searchBooks(Book book, int offset, int limit, Connection connection) {
//		BooksResponseData booksResponse = new BooksResponseData();
//	
//		try {
//			PreparedStatement ps = prepareSelectStatement(book, offset, limit, connection);
//			ResultSet rs = ps.executeQuery();
//			while (rs.next()) {
//				Book newBook = new Book(rs);
//				newBook.setAuthor(Author.selectAuthorNameByISBN(newBook.getBookISBN(), connection));
//				newBook.setPublisherName(Publisher.selectPublisherById(newBook.getPublisherId(), connection));
//				booksResponse.addBook(newBook);
//			}
//		} catch (SQLException e) {
//			booksResponse.setError(e.getMessage());
//			e.printStackTrace();
//		}
//		return booksResponse;
//	}
//	
//	private static PreparedStatement prepareSelectStatement (Book book, int offset, int limit, Connection connection) throws SQLException {
//		boolean priceFound = false;
//		boolean yearFound = false;
//		String query = String.format(SELECT_All, BOOK_TABLE, Author.BOOK_AUTHORS_TABLE, Author.AUTHOR_TABLE,
//				Publisher.PUBLISHER_TABLE, BOOK_CATEGORY_TABLE);
//		int index = S_ISBN_INDEX;
//		if (!book.getPublicationYear().equals("")) {
//			query += "AND PUBLICATION_YEAR = ?";
//			yearFound = true;
//		}
//		
//		if (book.getSellingPrice() != -1) {
//			query += "AND SELLING_PRICE = ?";
//			priceFound = true;
//		}
//		query += " LIMIT %d, %d;";
//		query = String.format(query, offset, limit);
//		PreparedStatement ps = connection.prepareStatement(query);
//		ps.setString(S_AUTHORFN_INDEX, "%" + book.getAuthor().getName() + "%");
//		ps.setString(S_AUTHORLN_INDEX, "%" + book.getAuthor().getLastName() + "%");
//		ps.setString(S_CATEGORY_INDEX, "%" + book.getCategory() + "%");
//		ps.setString(S_TITLE_INDEX,"%" +  book.getBookTitle() + "%");
//		ps.setString(S_PUBLISHER_INDEX, "%" +  book.getPublisherName() + "%");
//		ps.setString(S_ISBN_INDEX, "%" + book.getBookISBN() + "%");
//		if (yearFound) {
//			index++;
//			ps.setInt(index, Integer.parseInt(book.getPublicationYear()));
//		}
//		if (priceFound) {
//			index++;
//			ps.setFloat(index, book.getSellingPrice());
//
//		}	
////		System.out.println(ps.toString());
//		return ps;
//	}

	public ResponseData bookAddition(Connection connection) {
		boolean isBookExisting = BookModel.selectBookByISBN(bookISBN, connection);
		ResponseData response = new ResponseData();
		if (isBookExisting) {
			response.setError(BookError.IVALID_BOOK_ISBN.toString());
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
			int rowsAffected = query.getUpdateCount();
			return response;
		} catch (SQLException | NumberFormatException e) {
			response.setError(SqlError.DUPLICATE_KEY.toString());
			return response;
		} finally {
			query.close();
		}
	}

}
