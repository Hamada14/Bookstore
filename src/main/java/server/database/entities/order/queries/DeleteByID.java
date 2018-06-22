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
public class DeleteByID extends Query{

	private static final String DELETE_BY_ID = "DELETE FROM %s WHERE ORDER_ID = ?";
	
	private int id;
	@Override
	protected void prepareStatement(Connection connection) throws SQLException {
		String query = String.format(DELETE_BY_ID, OrderModel.BOOK_ORDER_TABLE);
		ps = (PreparedStatement) connection.prepareStatement(query);
		ps.setInt(1, id);
	}

}
