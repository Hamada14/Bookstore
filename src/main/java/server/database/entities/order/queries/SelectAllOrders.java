package server.database.entities.order.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import lombok.Getter;
import lombok.Setter;
import server.database.Query;
import server.database.entities.order.OrderModel;

@Getter
@Setter
public class SelectAllOrders extends Query{

	private static final String SELECT_ALL_ORDERS = "SELECT * FROM %s LIMIT %d,%d";
	
	private int offset;
	private int limit;
	@Override
	protected void prepareStatement(Connection connection) throws SQLException {
		String query = String.format(SELECT_ALL_ORDERS, OrderModel.BOOK_ORDER_TABLE, offset, limit);
		ps = (PreparedStatement) connection.prepareStatement(query);
	}

}
