package server.database.entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import server.ResponseData;

public class Order {

	private static final String PLACE_ORDER_QUERY = "insert into %s(BOOK_ISBN, QUANTITY) values(?, ?)";
	private static final String BOOK_ORDER_TABLE = "BOOK_ORDER";
	
	private int quantity;
	private Book book;
	
	public Order (int quantity, Book book) {
		this.quantity = quantity;
		this.book = book;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Book getBook() {
		return book;
	}
	
	public static boolean addNewOrder(Order order, Connection connection) {
		try {
			String query = String.format(PLACE_ORDER_QUERY, BOOK_ORDER_TABLE);
			PreparedStatement st = (PreparedStatement) connection.prepareStatement(query);
			st.setString(1, order.book.getBookISBN());
			st.setLong(2, order.quantity);
			st.executeUpdate();
		} catch(SQLException | NumberFormatException sql) {
			return false;
		}
		return true;
	}

}
