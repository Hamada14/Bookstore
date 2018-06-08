package server.database.entities;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Getter;
import lombok.Setter;
import server.BooksResponseData;

@Getter
@Setter
public class Book {

	/* should be changed to be loaded from database at start of system */
	public static final String[] BOOK_CATEGORIES = new String[] {"ALL", "Art", "Geography", "History", "Religion",
			"Science" };
	private static final String ISBN_REGEX = "^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$";
	private static final String INSERT_BOOK = "INSERT INTO BOOK(ISBN, TITLE, PUBLISHER_ID, PUBLICATION_YEAR,"
			+ " SELLING_PRICE, CATEGORY, MIN_THRESHOLD, QUANTITY)" + " VALUES(?,?,?,?,?,?,?,?);";
	private static final String SELECT_BOOK = "SELECT * FROM %s";
	private static final String INSERT_AUTHOR_REF = "INSERT INTO %s (BOOK_ISBN, AUTHOR_ID) VALUES (?, ?)";

	private static final String SELECT_All = "SELECT  * FROM %s";

	private static final String SELECT_CATEGORY = "SELECT ID FROM %s WHERE CATEGORY=?";
	private static final String BOOK_CATEGORY_TABLE = "BOOK_CATEGORY";
	private static final String BOOK_AUTHOR = "BOOK_AUTHOR";
	private static final String BOOK_TABLE = "BOOK";
	private static final String ID_COL = "ID";
	private static final int ISBN_INDEX = 1;
	private static final int BOOK_TITLE_INDEX = 2;
	private static final int PUBLISHER_ID_INDEX = 3;
	private static final int PUBLICATION_YEAR_INDEX = 4;
	private static final int SELLING_PRICE_INDEX = 5;
	private static final int CATEGORY_INDEX = 6;
	private static final int MIN_THRESHOLD_INDEX = 7;
	private static final int QUANTITY_INDEX = 8;

	private static final int BOOK_AUTHORS_ISBN_INDEX = 1;
	private static final int BOOK_AUTHORS_ID_INDEX = 2;

	private static final int ORIGINAL_QUANTITY = 0;
	
	private static final int CATEGORY_NOT_FOUND = -1;


	private static final float MAX_SELLING_PRICE = 999999.99f;
	private static final float MIN_SELLING_PRICE = 0.00f;

	private String bookISBN;
	private String bookTitle;
	private String publicationYear;
	private float sellingPrice;
	private String category;
	private int publisherId;
	private int quantity;
	private int minimumThreshold;

	public Book(String bookISBN, String bookTitle, String publicationYear, float sellingPrice, String category,
			String publisherName, int quantity, int minimumThreshold) {
		super();
		this.bookISBN = bookISBN;
		this.bookTitle = bookTitle;
		this.publicationYear = publicationYear;
		this.sellingPrice = sellingPrice;
		this.category = category;
		this.quantity = quantity;
		this.minimumThreshold = minimumThreshold;
	}

	public Book() {

	}

	public Book(ResultSet rs) throws SQLException {
		this.bookISBN = rs.getString(ISBN_INDEX);
		this.bookTitle = rs.getString(BOOK_TITLE_INDEX);
		this.publicationYear = Integer.toString(rs.getInt(PUBLICATION_YEAR_INDEX));
		this.sellingPrice = rs.getFloat(SELLING_PRICE_INDEX);
		this.category = BOOK_CATEGORIES[rs.getInt(CATEGORY_INDEX)];
		this.quantity = rs.getInt(QUANTITY_INDEX);
		this.publisherId = rs.getInt(PUBLISHER_ID_INDEX);
		this.minimumThreshold = rs.getInt(MIN_THRESHOLD_INDEX);

	}

	public Book(String title) {
		this.bookTitle = title;
	}

	public static BooksResponseData searchBooks(String filter, String valueFilter, Connection connection) {
		BooksResponseData booksResponse = new BooksResponseData();
		String query = String.format(SELECT_All, BOOK_TABLE);
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Book newBook = new Book(rs);
				booksResponse.addBook(newBook);
			}
		} catch (SQLException e) {
		    booksResponse.setError(e.getMessage());
			e.printStackTrace();
		}	
	 return booksResponse;
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
		if (isBookExisting) {
			return false;
		}
		int categoryId = getCategoryId(book.getCategory(), connection);
		if(categoryId == CATEGORY_NOT_FOUND) {
			return false;
		}
		try {
			int pubYear = Integer.valueOf(book.getPublicationYear());
			String query = String.format(INSERT_BOOK, BOOK_TABLE);
			PreparedStatement st = (PreparedStatement) connection.prepareStatement(query);
			st.setString(ISBN_INDEX, book.getBookISBN());
			st.setInt(CATEGORY_INDEX, categoryId);
			st.setString(BOOK_TITLE_INDEX, book.getBookTitle());
			st.setInt(PUBLISHER_ID_INDEX, book.getPublisherId());
			st.setInt(PUBLICATION_YEAR_INDEX, pubYear);
			st.setInt(QUANTITY_INDEX, ORIGINAL_QUANTITY);
			st.setInt(MIN_THRESHOLD_INDEX, book.getMinimumThreshold());
			st.setFloat(SELLING_PRICE_INDEX, book.getSellingPrice());
			System.out.println("HEREEEEEEE1" + st);
			int rowsAffected = st.executeUpdate();
			System.out.println("HEREEEEEEE1");
			return rowsAffected == 1;
		} catch (SQLException | NumberFormatException e) {
			return false;
		}
	}

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

	private static boolean isValidSellingPrice(float value) {
		return value >= MIN_SELLING_PRICE && value <= MAX_SELLING_PRICE;
	}

	private static boolean isValidPublicationYear(String strValue) {
		int value;
		try {
			value = Integer.valueOf(strValue);
		} catch (NumberFormatException e) {
			return false;
		}
		Calendar now = Calendar.getInstance();
		return value >= 0 && value <= now.get(Calendar.YEAR);
	}

	private static boolean isValidISBN(String isbn) {
		Pattern pattern = Pattern.compile(ISBN_REGEX);
		Matcher matcher = pattern.matcher(isbn);
		return matcher.matches();
	}

}
