package server.database.entities.book;

import java.io.Serializable;
import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
<<<<<<< HEAD
import java.util.Calendar;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
=======
>>>>>>> new-design

import lombok.Getter;
import lombok.Setter;
import server.database.entities.author.Author;
import server.database.entities.book.query.AddBook;
import server.database.entities.publisher.Publisher;
import server.database.entities.publisher.PublisherModel;

@Getter
@Setter
public class Book implements Serializable {

<<<<<<< HEAD
	/**
	 * 
	 */
	private static final long serialVersionUID = 7418014002918381057L;
	/* should be changed to be loaded from database at start of system */
	public static final String[] BOOK_CATEGORIES = new String[] { "", "Art", "Geography", "History", "Religion",
			"Science" };
	private static final String ISBN_REGEX = "^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$";
	private static final String INSERT_BOOK = "INSERT INTO BOOK(ISBN, TITLE, PUBLISHER_ID, PUBLICATION_YEAR,"
			+ " SELLING_PRICE, CATEGORY, MIN_THRESHOLD, QUANTITY)" + " VALUES(?,?,?,?,?,?,?,?);";
	private static final String SELECT_BOOK = "SELECT * FROM %s";
	private static final String INSERT_AUTHOR_REF = "INSERT INTO %s (BOOK_ISBN, AUTHOR_ID) VALUES (?, ?)";

	private static final String SELECT_All = "SELECT  * FROM %s Where ISBN IN"
			+ " (SELECT BOOK_ISBN From %s JOIN %s ON AUTHOR_ID = ID"
			+ " WHERE FIRST_NAME LIKE ? AND LAST_NAME LIKE ?) "
			+ " AND PUBLISHER_ID IN (SELECT ID FROM %s WHERE NAME LIKE ?)"
			+ " AND CATEGORY IN (SELECT ID FROM %s WHERE CATEGORY LIKE ?)"
			+ " AND TITLE LIKE ? AND ISBN LIKE ?";
	private static final int S_AUTHORFN_INDEX = 1;
	private static final int S_AUTHORLN_INDEX = 2;
	private static final int S_PUBLISHER_INDEX = 3;
	private static final int S_CATEGORY_INDEX = 4;
	private static final int S_TITLE_INDEX = 5;
	private static final int S_ISBN_INDEX = 6;


	private static final String SELECT_CATEGORY = "SELECT ID FROM %s WHERE CATEGORY=?";
	private static final String BOOK_CATEGORY_TABLE = "BOOK_CATEGORY";
	private static final String BOOK_AUTHOR = "BOOK_AUTHOR";
	private static final String BOOK_TABLE = "BOOK";
	private static final String ID_COL = "ID";
=======
	private static final long serialVersionUID = -613193187079216677L;
	
	
>>>>>>> new-design
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
		this.publisherName = publisherName;
	}

<<<<<<< HEAD
	public Book() {

	}

	public Book(String title) {
		this.bookTitle = title;
	}

	public Book(ResultSet rs) throws SQLException {
=======
	public Book(ResultSet rs, Connection connection) throws SQLException {
>>>>>>> new-design
		this.bookISBN = rs.getString(ISBN_INDEX);
		this.bookTitle = rs.getString(BOOK_TITLE_INDEX);
		this.publicationYear = Integer.toString(rs.getInt(PUBLICATION_YEAR_INDEX));
		this.sellingPrice = rs.getFloat(SELLING_PRICE_INDEX);
		this.category = BookModel.getCategory(rs.getInt(CATEGORY_INDEX), connection);
		this.quantity = rs.getInt(QUANTITY_INDEX);
		this.publisher = PublisherModel.getPublisherByID(rs.getInt(PUBLISHER_ID_INDEX), connection);
		this.minimumThreshold = rs.getInt(MIN_THRESHOLD_INDEX);

	}

<<<<<<< HEAD
	public static BooksResponseData searchBooks(Book book, int offset, int limit, Connection connection) {
		BooksResponseData booksResponse = new BooksResponseData();
	
		try {
			PreparedStatement ps = prepareSelectStatement(book, offset, limit, connection);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Book newBook = new Book(rs);
				newBook.setAuthor(Author.selectAuthorNameByISBN(newBook.getBookISBN(), connection));
				newBook.setPublisherName(Publisher.selectPublisherById(newBook.getPublisherId(), connection));
				booksResponse.addBook(newBook);
			}
		} catch (SQLException e) {
			booksResponse.setError(e.getMessage());
			e.printStackTrace();
		}
		return booksResponse;
	}
	
	private static PreparedStatement prepareSelectStatement (Book book, int offset, int limit, Connection connection) throws SQLException {
		boolean priceFound = false;
		boolean yearFound = false;
		String query = String.format(SELECT_All, BOOK_TABLE, Author.BOOK_AUTHORS_TABLE, Author.AUTHOR_TABLE,
				Publisher.PUBLISHER_TABLE, BOOK_CATEGORY_TABLE);
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
		PreparedStatement ps = connection.prepareStatement(query);
		ps.setString(S_AUTHORFN_INDEX, "%" + book.getAuthor().getName() + "%");
		ps.setString(S_AUTHORLN_INDEX, "%" + book.getAuthor().getLastName() + "%");
		ps.setString(S_CATEGORY_INDEX, "%" + book.getCategory() + "%");
		ps.setString(S_TITLE_INDEX,"%" +  book.getBookTitle() + "%");
		ps.setString(S_PUBLISHER_INDEX, "%" +  book.getPublisherName() + "%");
		ps.setString(S_ISBN_INDEX, "%" + book.getBookISBN() + "%");
		if (yearFound) {
			index++;
			ps.setInt(index, Integer.parseInt(book.getPublicationYear()));
		}
		if (priceFound) {
			index++;
			ps.setFloat(index, book.getSellingPrice());

		}	
//		System.out.println(ps.toString());
		return ps;
	}

	public static final boolean addBook(Book book, int authorId, Connection connection) {
		boolean validBook = validateBookAttributes(book);
		if (!validBook) {
			return false;
		}
		boolean bookAdded = bookAddition(book, authorId, connection);
		if (bookAdded) {
			return authorRefAddition(book.getBookISBN(), authorId, connection);
		}
		return false;
	}

	private static boolean authorRefAddition(String isbn, int authorId, Connection connection) {
		try {
			String query = String.format(INSERT_AUTHOR_REF, BOOK_AUTHOR);
			PreparedStatement st = (PreparedStatement) connection.prepareStatement(query);
			st.setString(BOOK_AUTHORS_ISBN_INDEX, isbn);
			st.setInt(BOOK_AUTHORS_ID_INDEX, authorId);
			int rowsAffected = st.executeUpdate();
			return rowsAffected != 0;
		} catch (SQLException e) {
			return false;
		}
	}

	private static boolean bookAddition(Book book, int authorId, Connection connection) {
		boolean isBookExisting = selectBookByISBN(book.getBookISBN(), connection);
=======
	public boolean bookAddition(int authorId, Connection connection) {
		boolean isBookExisting = BookModel.selectBookByISBN(bookISBN, connection);
>>>>>>> new-design
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
			e.printStackTrace();
			return false;
		} finally {
			query.close();
		}
	}
<<<<<<< HEAD

	private static final int getCategoryId(String category, Connection connection) {
		try {
			String query = String.format(SELECT_CATEGORY, BOOK_CATEGORY_TABLE);
			PreparedStatement st = (PreparedStatement) connection.prepareStatement(query);
			st.setString(1, category);
			ResultSet rs = st.executeQuery();
			int id = CATEGORY_NOT_FOUND;
			while (rs.next()) {
				id = rs.getInt(ID_COL);
			}
			return id;
		} catch (SQLException e) {
			return CATEGORY_NOT_FOUND;
		}
	}

	private static boolean selectBookByISBN(String isbn, Connection connection) {
		try {
			String query = String.format(SELECT_BOOK, BOOK_TABLE);
			PreparedStatement st = (PreparedStatement) connection.prepareStatement(query);
			st.setString(ISBN_INDEX, isbn);
			ResultSet rs = st.executeQuery();
			return rs != null && rs.next();
		} catch (SQLException e) {
			return false;
		}
	}

	private static boolean validateBookAttributes(Book book) {
		return isValidISBN(book.getBookISBN()) && isValidPublicationYear(book.getPublicationYear())
				&& isValidSellingPrice(book.getSellingPrice()) && isValidMinimumThreshold(book.getMinimumThreshold());
	}

	private static boolean isValidMinimumThreshold(int val) {
		return val >= 0;
	}

	public static boolean isValidSellingPrice(float value) {
		return value >= MIN_SELLING_PRICE && value <= MAX_SELLING_PRICE;
	}

	public static boolean isValidPublicationYear(String strValue) {
		int value;
		try {
			value = Integer.valueOf(strValue);
		} catch (NumberFormatException e) {
			return false;
		}
		Calendar now = Calendar.getInstance();
		return value >= 0 && value <= now.get(Calendar.YEAR);
	}

	public static boolean isValidISBN(String isbn) {
		Pattern pattern = Pattern.compile(ISBN_REGEX);
		Matcher matcher = pattern.matcher(isbn);
		return matcher.matches();
	}

=======
>>>>>>> new-design
}
