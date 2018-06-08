package server.database.entities.publisher.query;

import java.sql.Connection;
import java.sql.SQLException;

import lombok.Setter;
import server.database.Query;
import server.database.entities.publisher.PublisherModel;

public class PublisherByID extends Query {
	private static final String PUBLISHER_BY_ID = "SELECT * FROM %s WHERE ID = ?;";
	private static final int ID_INDEX = 1;
	
	@Setter private int id;
	
	@Override
	protected void prepareStatement(Connection connection) throws SQLException {
		String query = String.format(PUBLISHER_BY_ID, PublisherModel.PUBLISHER_TABLE);
		ps = connection.prepareStatement(query);
		ps.setInt(ID_INDEX, id);
	}

}
