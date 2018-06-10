package server.database.entities.publisher.query;

import java.sql.Connection;
import java.sql.SQLException;

import lombok.Setter;
import server.database.Query;

public class SelectPhoneById extends Query{

	private static final String SELECT_PHONE = "SELECT PHONE FROM %s WHERE ID = ?";
	private static final String PHONE_TABLE = "PUBLISHER_PHONE";
	
	private static final int ID_IND = 1;
	
	@Setter private int id;

	@Override
	protected void prepareStatement(Connection connection) throws SQLException {
		String query = String.format(SELECT_PHONE, PHONE_TABLE);
		ps = connection.prepareStatement(query);
		ps.setInt(ID_IND, id);
	}
	
	
	
}
