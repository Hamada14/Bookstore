package server.database.entities;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Order {

	private static final String PLACE_ORDER_QUERY = "insert into %s(BOOK_ISBN, QUANTITY) values(?, ?)";
	private static final String BOOK_ORDER_TABLE = "BOOK_ORDER";
	
	private int quantity;
	private Book book;
	
	public Order (int quantity, Book book) {
		this.quantity = quantity;
		this.book = book;
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

	 @Override
     public int hashCode() {
		 return book.hashCode();
	 }
	 
	 @Override
	 public boolean equals(Object obj) {
		 if (this == obj)
             return true;
         if (obj == null)
             return false;
         if (getClass() != obj.getClass())
             return false;
         Order other = (Order) obj;
         return this.getBook().getBookISBN().equals(other.getBook().getBookISBN());
	 }
	 
}
