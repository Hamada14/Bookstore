package server.database.entities;

public class Order {

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

}
