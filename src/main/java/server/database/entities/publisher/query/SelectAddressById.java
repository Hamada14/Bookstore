package server.database.entities.publisher.query;

import java.sql.Connection;
import java.sql.SQLException;

import lombok.Setter;
import server.database.Query;

public class SelectAddressById extends Query {

	private static final String SELECT_ADDRESS = "SELECT STREET, CITY, COUNTRY FROM %s WHERE ID = ?";
	private static final String ADDRESS_TABLE = "PUBLISHERS_ADDRESS";
	
	private static final int ID_IND = 1;
	
	@Setter private int id;

	@Override
	protected void prepareStatement(Connection connection) throws SQLException {
		String query = String.format(SELECT_ADDRESS, ADDRESS_TABLE);
		ps = connection.prepareStatement(query);
		ps.setInt(ID_IND, id);
	}
	
	
	
}
