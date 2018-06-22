package server.database.entities.shoppingcart.queries;

import java.sql.Connection;

import java.sql.SQLException;

import lombok.Getter;
import lombok.Setter;
import server.database.Query;
import server.database.entities.order.Order;

@Getter
@Setter
public class InsertShoppingItem extends Query{

	private static final String INSERT_NEW_ITEM = "INSERT INTO  %s (SHOPPING_ORDER_ID, BOOK_ISBN, QUANTITY, SELLING_PRICE) "
			+ " VALUES (?, ?, ?, ?)";
	private static final String ITEMS_TABLE = "SHOPPING_ORDER_ITEM";

	private static final int ORDER_INDEX = 1;
	private static final int BOOK_ISBN_INDEX = 2;
	private static final int QUANTITY_INDEX = 3;
	private static final int SELLING_PRICE_INDEX = 4;
	
	
	private Order order;
	private int orderID;
	
	@Override
	protected void prepareStatement(Connection connection) throws SQLException {
		String query = String.format(INSERT_NEW_ITEM, ITEMS_TABLE);
		ps = connection.prepareStatement(query);
		ps.setInt(ORDER_INDEX, orderID);
		ps.setString(BOOK_ISBN_INDEX, order.getBook().getBookISBN());
		ps.setInt(QUANTITY_INDEX, order.getQuantity());
		ps.setFloat(SELLING_PRICE_INDEX, order.getBook().getSellingPrice());
		
	}

}
