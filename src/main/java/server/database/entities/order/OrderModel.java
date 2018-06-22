package server.database.entities.order;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import server.ResponseData;
import server.database.entities.order.queries.*;
import server.errors.OrderError;
import server.errors.SqlError;

public class OrderModel {
	
	public static final String BOOK_ORDER_TABLE = "BOOK_ORDER";
	private static final String INVALID_QUANTITY = "Invalid quantity";
	
	
	public static ResponseData addNewOrder(Order order, Connection connection) {
		ResponseData rs = new ResponseData();
		if(order.getQuantity() <= 0) {
			rs.setError(INVALID_QUANTITY);
			return rs;
		}
		try {		
			AddNewOrder query = new AddNewOrder();
			query.setOrder(order);
			query.executeQuery(connection);
			return rs;
		} catch(SQLException | NumberFormatException sql) {
			rs.setError(SqlError.SERVER_ERROR.toString());
			return rs;
		}
	}

	public static List<Order> selectAllOrders(int offset, int limit, Connection connection) {
		try {
			SelectAllOrders query = new SelectAllOrders();
			query.setLimit(limit);
			query.setOffset(offset);
			query.executeQuery(connection);
			ResultSet rs = query.getResultSet();
			return composeList(rs);
		} catch(SQLException e) {
			return null;
		}
	}
	
	private static List<Order> composeList(ResultSet rs) throws SQLException {
		List<Order> orders = new ArrayList<>();
		while(rs.next()) {
			Order order = new Order(rs);
			orders.add(order);
		}
		return orders;
	}
	
	public static ResponseData deleteOrderById(int id, Connection connection) {
		ResponseData responseData = new ResponseData();
		try {
			
			DeleteByID query = new DeleteByID();
			query.setId(id);
			query.executeQuery(connection);
			int rowsAffected = query.getUpdateCount();
			if(rowsAffected != 1) {
				throw new SQLException();
			}
			return responseData;
		} catch(SQLException e) {
			responseData.setError(OrderError.ERROR_DELETING_ORDER.toString());
			return responseData;
		}
	}

}
