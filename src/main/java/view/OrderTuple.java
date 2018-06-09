package view;

import javafx.scene.control.Hyperlink;
import lombok.Getter;
import lombok.Setter;
import server.database.entities.Order;

@Setter
@Getter
public class OrderTuple {
	
	private String bookISBN;
	private Integer quantity;
	private Hyperlink confirm;
	private Order order;
	private int id;
	
	public OrderTuple(Order order, Hyperlink confirm) {
		this.order = order;
		this.bookISBN = order.getBook().getBookISBN();
		this.quantity = order.getQuantity();
		this.confirm = confirm;
		id = order.getOrderId();
	}
	
}

