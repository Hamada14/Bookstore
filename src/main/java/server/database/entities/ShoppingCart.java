package server.database.entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import server.ResponseData;

public class ShoppingCart implements Iterable<Order> {

	private Set<Order> orders;

	private static final String INSERT_NEW_ORDER = "INSERT INTO  %s (USER_NAME) VALUES (?);";
	private static final String ORDERS_TABLE = "SHOPPING_ORDER";
	private static final int USERNAME_INDEX = 1;

	private static final String INSERT_NEW_ITEM = "INSERT INTO  %s (SHOPPING_ORDER_ID, BOOK_ISBN, QUANTITY, SELLING_PRICE) "
			+ " VALUES (?, ?, ?, ?)";
	private static final String ITEMS_TABLE = "SHOPPING_ORDER_ITEM";
	public static final String SHORTAGE_CODE = "45000";
	public static final String ERROR_CONNECTION_CODE = "00000";
	private static final int ORDER_INDEX = 1;
	private static final int BOOK_ISBN_INDEX = 2;
	private static final int QUANTITY_INDEX = 3;
	private static final int SELLING_PRICE_INDEX = 4;
	private static final int TRY_ROLLBACK = 10;
	private static final int TRY_TURN_ON_COMMIT = 10;
	
	public ShoppingCart() {
		orders = new HashSet<>();
	}

	public void addOrder(Order newOrder) {

		if (!orders.contains(newOrder)) {
			orders.add(newOrder);
		} else {
			orders.remove(newOrder);
			orders.add(newOrder);
		}
	}

	public void removeOrder(Order removedOrder) {
		orders.remove(removedOrder);
	}

	public void clearCart() {
		orders.clear();
	}

	@Override
	public Iterator<Order> iterator() {
		// TODO Auto-generated method stub
		return orders.iterator();
	}

	public int getSize() {
		return orders.size();
	}

	private PreparedStatement insertNewOrder(String userName, Connection connection) throws SQLException {
		PreparedStatement ps = null;
		String query = String.format(INSERT_NEW_ORDER, ORDERS_TABLE);
		ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		ps.setString(USERNAME_INDEX, userName);
		ps.executeUpdate();

		return ps;
	}

	private PreparedStatement insertShoppingItem(Order order, int orderID, Connection connection) throws SQLException {
		String query = String.format(INSERT_NEW_ITEM, ITEMS_TABLE);
		PreparedStatement ps = connection.prepareStatement(query);
		ps.setInt(ORDER_INDEX, orderID);
		ps.setString(BOOK_ISBN_INDEX, order.getBook().getBookISBN());
		ps.setInt(QUANTITY_INDEX, order.getQuantity());
		ps.setFloat(SELLING_PRICE_INDEX, order.getBook().getSellingPrice());
		ps.executeUpdate();
		return ps;
	}

	public ResponseData checkOut(String userName, Connection connection) {
		ResponseData rs = new ResponseData();
		ArrayList<PreparedStatement> curStmts = new ArrayList<>();
		try {
			connection.setAutoCommit(false);
			PreparedStatement insertOrderStmt = insertNewOrder(userName, connection);
			curStmts.add(insertOrderStmt);
			int orderID = 0;
			ResultSet generatedKeys = insertOrderStmt.getGeneratedKeys();
			if (generatedKeys.next()) {
				orderID = generatedKeys.getInt(1);
			}
			Iterator<Order> iter = this.iterator();
			while (iter.hasNext()) {
				Order order = (Order) iter.next();
				PreparedStatement insertItemStmt = insertShoppingItem(order, orderID, connection);
				curStmts.add(insertItemStmt);
			}
			connection.commit();
		} catch (SQLException e) {
			if (e.getSQLState().equals(SHORTAGE_CODE)) {
				rs.setError(SHORTAGE_CODE);
			} else {
				rs.setError(e.getMessage());
			}
			if (!rollBackTransaction(connection)) {
				rs.setError(ERROR_CONNECTION_CODE);
			}
			e.printStackTrace();
		}
		
		if(!turnAutoCommit(connection)) {
			rs.setError(ERROR_CONNECTION_CODE);
		}
		String error = closeStatements(curStmts);
		if (!error.equals("")) {
			rs.setError(error);
		}
		return rs;
	}
	
	private String closeStatements(ArrayList<PreparedStatement> stmts) {
		String error = "";
		for (PreparedStatement  stmt : stmts) {
			try {
				stmt.close();
			} catch (SQLException e) {
			   error += e.getMessage();
				e.printStackTrace();
			}
		}
		return error;
	}

	private boolean rollBackTransaction(Connection con) {
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

	private boolean turnAutoCommit(Connection con) {
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
}
