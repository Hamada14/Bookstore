package server.database.entities.book.query;

import java.sql.Connection;


import java.sql.SQLException;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import server.database.Query;
import server.database.entities.author.Author;
import server.database.entities.author.AuthorModel;
import server.database.entities.book.Book;
import server.database.entities.book.BookModel;
import server.database.entities.publisher.PublisherModel;

@Setter
@Getter
public class AdvancedSelection extends Query {
	private static final String SELECT_All = "SELECT  * FROM %s WHERE "
			+ "PUBLISHER_ID IN (SELECT ID FROM %s WHERE NAME LIKE ?) "
			+ "AND CATEGORY IN (SELECT ID FROM %s WHERE CATEGORY LIKE ?)" + " AND TITLE LIKE ? AND ISBN LIKE ? ";
	private static final String AUTHORS_HEADER = "AND ISBN IN ( ";
	private static final String SELECT_BY_AUTHOR_NAME = "SELECT BOOK_ISBN From %s JOIN %s ON AUTHOR_ID = ID" + " WHERE NAME LIKE ? ";
	private static final int S_PUBLISHER_INDEX = 1;
	private static final int S_CATEGORY_INDEX = 2;
	private static final int S_TITLE_INDEX = 3;
	private static final int S_ISBN_INDEX = 4;

	private Book book;
	private int offset;
	private int limit;

	@Override
	protected void prepareStatement(Connection connection) throws SQLException {
		boolean priceFound = false;
		boolean yearFound = false;
		String query = String.format(SELECT_All, BookModel.BOOK_TABLE, 
				PublisherModel.PUBLISHER_TABLE, BookModel.BOOK_CATEGORY_TABLE);
		
		query = addAuthorsSelectionQuery(query, book.getAuthors());
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
		ps.setString(S_CATEGORY_INDEX, "%" + book.getCategory() + "%");
		ps.setString(S_TITLE_INDEX, "%" + book.getBookTitle() + "%");
		ps.setString(S_PUBLISHER_INDEX, "%" + book.getPublisher().getName() + "%");
		ps.setString(S_ISBN_INDEX, "%" + book.getBookISBN() + "%");
		int index = S_ISBN_INDEX;
		index = setAuthorsSelectionStatment(book.getAuthors(), index);
		if (yearFound) {
			index++;
			ps.setInt(index, Integer.parseInt(book.getPublicationYear()));
		}
		if (priceFound) {
			index++;
			ps.setFloat(index, book.getSellingPrice());
		}
	}
	

	private String addAuthorsSelectionQuery(String query, List<Author> list) {
		if (!list.isEmpty()) {
			query += AUTHORS_HEADER;
			for (int i = 1; i < list.size(); i++) {
				query += SELECT_BY_AUTHOR_NAME + ") AND ISBN IN (";
				query = String.format(query, BookModel.BOOK_AUTHOR, AuthorModel.AUTHOR_TABLE);
			}
			query += SELECT_BY_AUTHOR_NAME +" )";
			query = String.format(query, BookModel.BOOK_AUTHOR, AuthorModel.AUTHOR_TABLE);	
			
		}
		return query;
	}
	
	private int setAuthorsSelectionStatment(List<Author> authors, int index) throws SQLException {
		int newIndex = index;
		if (!authors.isEmpty()) {		
			for (int i = 0; i < authors.size(); i++) {
				newIndex++;
				ps.setString(newIndex, "%" + authors.get(i).getName() + "%");
			}
		}
		return newIndex;
	}
}
