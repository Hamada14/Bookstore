package server.database.entities.publisher.query;

import java.sql.Connection;
import java.sql.SQLException;

import lombok.Setter;
import server.database.Query;
import server.database.entities.publisher.PublisherModel;

public class AddPublisher extends Query {

	private static final String ADD_PUBLISHSER = "INSERT INTO %s (NAME) VALUES (?)";
	private static final int NAME_INDEX = 1;
	
	@Setter private String name;
	
	@Override
	protected void prepareStatement(Connection connection) throws SQLException {
		String query = String.format(ADD_PUBLISHSER, PublisherModel.PUBLISHER_TABLE);
		ps = connection.prepareStatement(query);
		ps.setString(NAME_INDEX, name);
	}
}
