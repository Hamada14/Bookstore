package server.database.entities.shoppingcart.queries;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import lombok.Getter;
import lombok.Setter;
import server.database.Query;

@Getter
@Setter
public class InsertShoppingOrder extends Query {
	
	
	private static final String INSERT_NEW_ORDER = "INSERT INTO  %s (USER_NAME) VALUES (?);";
	private static final String ORDERS_TABLE = "SHOPPING_ORDER";
	private static final int USERNAME_INDEX = 1;
	private String userName;

	@Override
	protected void prepareStatement(Connection connection) throws SQLException {
		String query = String.format(INSERT_NEW_ORDER, ORDERS_TABLE);
		ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		ps.setString(USERNAME_INDEX, userName);
	}
	
	
}
