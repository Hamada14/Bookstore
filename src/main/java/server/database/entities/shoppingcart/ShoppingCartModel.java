package server.database.entities.shoppingcart;

import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import server.OrderResponseData;
import server.database.Query;
import server.database.entities.order.Order;
import server.database.entities.book.query.SelectQuantityByISBN;
import server.database.entities.shoppingcart.queries.InsertShoppingItem;
import server.database.entities.shoppingcart.queries.InsertShoppingOrder;

public class ShoppingCartModel {
	private static final int TRY_ROLLBACK = 10;
	private static final int TRY_TURN_ON_COMMIT = 10;
	public static final String SHORTAGE_CODE = "45000";
	public static final String ERROR_CONNECTION_CODE = "00000";

	public static OrderResponseData checkOut(ShoppingCart cart, String userName, Connection connection) {
		OrderResponseData rs = new OrderResponseData();
		ArrayList<Query> curStmts = new ArrayList<>();
		Order order = null;
		try {
			connection.setAutoCommit(false);
			InsertShoppingOrder insertOrderStmt = new InsertShoppingOrder();
			insertOrderStmt.setUserName(userName);
			insertOrderStmt.executeQuery(connection);
			curStmts.add(insertOrderStmt);
			int orderID = 0;
			ResultSet generatedKeys = insertOrderStmt.getGeneratedKeys();
			if (generatedKeys.next()) {
				orderID = generatedKeys.getInt(1);
			}
			Iterator<Order> iter = cart.getOrders().iterator();
			while (iter.hasNext()) {
				order = (Order) iter.next();
				InsertShoppingItem insertItemStmt = new InsertShoppingItem();
				insertItemStmt.setOrder(order);
				insertItemStmt.setOrderID(orderID);
				insertItemStmt.executeQuery(connection);
				curStmts.add(insertItemStmt);
			}
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			rs = handleError(order, e, connection);
			if (!rollBackTransaction(connection)) {
				rs.setError(ERROR_CONNECTION_CODE);
			}
		}

		if (!turnAutoCommit(connection)) {
			rs.setError(ERROR_CONNECTION_CODE);
		}
		closeStatements(curStmts);
		return rs;
	}

	private static String closeStatements(ArrayList<Query> stmts) {
		String error = "";
		for (Query stmt : stmts) {
			stmt.close();
		}
		return error;
	}

	private static boolean rollBackTransaction(Connection con) {
		for (int i = 1; i <= TRY_ROLLBACK; i++) {
			try {
				con.rollback();
				return true;
			} catch (SQLException sql) {
				sql.printStackTrace();
			}
		}
		return false;
	}

	private static boolean turnAutoCommit(Connection con) {
		for (int i = 1; i <= TRY_TURN_ON_COMMIT; i++) {
			try {
				con.setAutoCommit(true);
				return true;
			} catch (SQLException sql) {
				sql.printStackTrace();
			}
		}
		return false;
	}

	private static OrderResponseData handleError(Order order, SQLException sql, Connection connection) {
		OrderResponseData rs = new OrderResponseData();
		if (sql.getSQLState().equals(SHORTAGE_CODE)) {
			return selectQuantity(order, connection);
		} else {
			rs.setError(sql.getMessage());
		}
		return null;

	}

	private static OrderResponseData selectQuantity(Order order, Connection connection) {
		OrderResponseData rs = new OrderResponseData();

		try {
			SelectQuantityByISBN query = new SelectQuantityByISBN();
			String isbn = order.getBook().getBookISBN();
			query.setIsbn(isbn);
			query.executeQuery(connection);
			ResultSet result = query.getResultSet();
			if (result.next()) {
				rs.setOldQuantity(result.getInt(1));
				rs.setOrder(order);
				rs.setError(SHORTAGE_CODE);
			}

		} catch (SQLException e) {
			rs.setError(e.getMessage());
		}
		return rs;
	}

}
