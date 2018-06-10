package server.database.entities.publisher.query;

import java.sql.Connection;
import java.sql.SQLException;

import lombok.Setter;
import server.database.Query;

public class DeletePublisherPhone extends Query{

	private static final String DELETE_PHONE = "DELETE FROM %s WHERE ID = ? AND PHONE = ?";
	private static final String PHONE_TABLE = "PUBLISHER_PHONE";
	
	private static final int ID_IND = 1;
	private static final int PHONE_IND = 2;
	
	@Setter private String number;
	@Setter private int id;
	
	@Override
	protected void prepareStatement(Connection connection) throws SQLException {
		String query = String.format(DELETE_PHONE, PHONE_TABLE);
		ps = connection.prepareStatement(query);
		ps.setInt(ID_IND, id);
		ps.setString(PHONE_IND, number);
	}

}
