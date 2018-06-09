package server.database.entities.book;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import server.BooksResponseData;
import server.ResponseData;
import server.database.entities.book.query.AddAuthorReference;
import server.database.entities.book.query.AllBooks;
import server.database.entities.book.query.AllCategories;
import server.database.entities.book.query.BookByISBN;
import server.database.entities.book.query.CategoryById;
import server.database.entities.book.query.CategoryIDByName;

public class BookModel {

	public static final String BOOK_TABLE = "BOOK";
	public static final String BOOK_CATEGORY_TABLE = "BOOK_CATEGORY";
	public static final String BOOK_AUTHOR = "BOOK_AUTHOR";

	public static final int CATEGORY_NOT_FOUND = -1;

	private static final int ID_COL = 1;
	private static final String CATEGORY_NAME_COL = "CATEGORY";

	public static BooksResponseData searchBooks(String filter, String valueFilter, Connection connection) {
		BooksResponseData booksResponse = new BooksResponseData();
		AllBooks query = new AllBooks();
		try {
			query.executeQuery(connection);
			ResultSet rs = query.getResultSet();
			while (rs.next()) {
				Book newBook = new Book(rs, connection);
				booksResponse.addBook(newBook);
			}
		} catch (SQLException e) {
			booksResponse.setError(e.getMessage());
			e.printStackTrace();
		} finally {
			query.close();
		}
		return booksResponse;
	}

	
	
	public static final ResponseData addBook(BookBuilder bookBuilder, Connection connection) {
		ResponseData response = bookBuilder.validateBookAttributes();
		if(!response.isSuccessful()) {
			return response;
		}
		Book book = bookBuilder.buildBook();
		return book.bookAddition(connection);
	}
	
	public static ResponseData editBook(BookBuilder modifiedBook, Connection connection) {
		ResponseData response = modifiedBook.validateBookAttributes();
		if(!response.isSuccessful()) {
			return response;
		}
		Book book = modifiedBook.buildBook();
		return book.editBook(connection);
	}

	private static boolean authorRefAddition(String isbn, int authorId, Connection connection) {
		try {
			AddAuthorReference query = new AddAuthorReference();
			query.setAuthorId(authorId);
			query.setIsbn(isbn);
			query.executeQuery(connection);
			int rowsAffected = query.getUpdateCount();
			return rowsAffected != 0;
		} catch (SQLException e) {
			return false;
		}
	}

	public static final int getCategoryId(String category, Connection connection) {
		try {
			CategoryIDByName query = new CategoryIDByName();
			query.setCategory(category);
			query.executeQuery(connection);
			ResultSet rs = query.getResultSet();
			int id = CATEGORY_NOT_FOUND;
			while (rs.next()) {
				id = rs.getInt(ID_COL);
			}
			return id;
		} catch (SQLException e) {
			return CATEGORY_NOT_FOUND;
		}
	}

	public static boolean selectBookByISBN(String isbn, Connection connection) {
		return getBookByISBN(isbn, connection) != null;
	}

	public static String getCategory(int id, Connection connection) {
		CategoryById query = new CategoryById();
		try {
			query.setId(id);
			query.executeQuery(connection);
			ResultSet rs = query.getResultSet();
			if (!rs.next()) {
				return null;
			}
			return rs.getString(CATEGORY_NAME_COL);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			query.close();
		}
	}

	public static List<String> getCategories(Connection connection) {
		List<String> categories = new ArrayList<>();
		AllCategories query = new AllCategories();
		try {
			query.executeQuery(connection);
			ResultSet rs = query.getResultSet();
			while(rs.next()) {
				categories.add(rs.getString(CATEGORY_NAME_COL));
			}
			return categories;
		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			query.close();
		}
	}
	
	public static Book getBookByISBN(String isbn, Connection connection) {
		BookByISBN query = new BookByISBN();
		try {
			query.setIsbn(isbn);
			query.executeQuery(connection);
			ResultSet rs = query.getResultSet();
			return new Book(rs, connection);
		} catch (SQLException e) {
			return null;
		} finally {
			query.close();
		}
	}



	public static BooksResponseData searchBooks(Book book, int offset, int limit, Connection connection) {
		// TODO Auto-generated method stub
		return null;
	}
}
