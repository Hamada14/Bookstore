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

import lombok.Getter;
import lombok.Setter;
import server.OrderResponseData;

@Setter
@Getter
public class ShoppingCart implements Iterable<Order> {

	private static final String BOOK_REMOVED = "Book is no longer on database";
	private static final String INSERT_NEW_ORDER = "INSERT INTO  %s (USER_NAME) VALUES (?);";
	private static final String ORDERS_TABLE = "SHOPPING_ORDER";
	private static final int USERNAME_INDEX = 1;

	private static final String INSERT_NEW_ITEM = "INSERT INTO  %s (SHOPPING_ORDER_ID, BOOK_ISBN, QUANTITY, SELLING_PRICE) "
			+ " VALUES (?, ?, ?, ?)";
	private static final String ITEMS_TABLE = "SHOPPING_ORDER_ITEM";
	
	private static final String SELECT_QUANTITY = "SELECT QUANTITY from %s WHERE ISBN = ?;";
	private static final String BOOKS_TABLE = "BOOK";
	
	public static final int NO_BOOK_CODE = 1216;
	public static final String SHORTAGE_CODE = "45000";
	public static final String ERROR_CONNECTION_CODE = "00000";
	private static final int ORDER_INDEX = 1;
	private static final int BOOK_ISBN_INDEX = 2;
	private static final int QUANTITY_INDEX = 3;
	private static final int SELLING_PRICE_INDEX = 4;
	private static final int TRY_ROLLBACK = 10;
	private static final int TRY_TURN_ON_COMMIT = 10;
	
	private Set<Order> orders;
	
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

	public OrderResponseData checkOut(String userName, Connection connection) {
		OrderResponseData rs = new OrderResponseData();
		ArrayList<PreparedStatement> curStmts = new ArrayList<>();
		Order order = null;
		try {
			connection.setAutoCommit(false);
			PreparedStatement insertOrderStmt = insertNewOrder(userName, connection);
			curStmts.add(insertOrderStmt);
			int orderID = 0;
			ResultSet generatedKeys = insertOrderStmt.getGeneratedKeys();
			if (generatedKeys.next()) {
				orderID = generatedKeys.getInt(1);
			}
			Iterator<Order> iter = orders.iterator();
			while (iter.hasNext()) {
				order = (Order) iter.next();
				PreparedStatement insertItemStmt = insertShoppingItem(order, orderID, connection);
				curStmts.add(insertItemStmt);
			}
			connection.commit();
		} catch (SQLException e) {			
			rs =  handleError(order, e, connection);
//			e.printStackTrace();	
			if (!rollBackTransaction(connection)) {
				rs.setError(ERROR_CONNECTION_CODE);
			}
//			e.printStackTrace();
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
	
	private OrderResponseData handleError (Order order, SQLException sql, Connection connection) {
		OrderResponseData rs = new OrderResponseData();
		if (sql.getSQLState().equals(SHORTAGE_CODE)) {
			return selectQuantity(order, connection);
		} else if (sql.getErrorCode() == NO_BOOK_CODE) {
			rs.setOrder(order);
			rs.setError(BOOK_REMOVED);
		}else {
			rs.setError(sql.getMessage());
		}
		return null;
		
	}
	
	private OrderResponseData selectQuantity (Order order, Connection connection) {
		OrderResponseData rs = new OrderResponseData();
		String query = String.format(SELECT_QUANTITY, BOOKS_TABLE);
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			String isbn = order.getBook().getBookISBN();
			ps.setString(1, isbn);
			ResultSet result = ps.executeQuery();
			if (!result.next()) {
				rs.setOrder(order);
				rs.setError(BOOK_REMOVED);
			} else {
				rs.setOldQuantity(result.getInt(1));
				rs.setOrder(order);
				rs.setError(SHORTAGE_CODE);
			}
		} catch (SQLException e) {
			rs.setError(e.getMessage());
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
