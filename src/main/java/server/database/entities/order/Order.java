package server.database.entities.order;

import java.io.Serializable;


import java.sql.ResultSet;
import java.sql.SQLException;


import lombok.Getter;
import lombok.Setter;

import server.database.entities.book.Book;


@Getter
@Setter
public class Order implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8540223415047834830L;
	
	private static final String QUANTITY_COL = "QUANTITY";
	private static final String BOOK_ISBN_COL = "BOOK_ISBN"; 
	private static final String ORDER_ID_COL = "ORDER_ID";
	
	
	private int quantity;
	private Book book;
	private int orderId;
	
	public Order (int quantity, Book book) {
		this.quantity = quantity;
		this.book = book;
	}
	
	public Order() {
		
	}
	
	public Order(ResultSet rs) throws SQLException {
		quantity = rs.getInt(QUANTITY_COL);
		Book book = new Book();
		book.setBookISBN(rs.getString(BOOK_ISBN_COL));
		orderId = rs.getInt(ORDER_ID_COL);
		this.book = book;
	}
	
	
	 @Override
     public int hashCode() {
		 return book.getBookISBN().hashCode();
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
