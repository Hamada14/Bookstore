package server.database.entities.order.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import lombok.Getter;
import lombok.Setter;
import server.database.Query;
import server.database.entities.order.Order;
import server.database.entities.order.OrderModel;

@Getter
@Setter
public class AddNewOrder extends Query{
	
	private Order order;
	private static final String PLACE_ORDER_QUERY = "insert into %s(BOOK_ISBN, QUANTITY) values(?, ?)";
	@Override
	protected void prepareStatement(Connection connection) throws SQLException {
		String query = String.format(PLACE_ORDER_QUERY, OrderModel.BOOK_ORDER_TABLE);
		ps = (PreparedStatement) connection.prepareStatement(query);
		ps.setString(1, order.getBook().getBookISBN());
		ps.setLong(2, order.getQuantity());
		
	}

}
