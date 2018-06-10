package server.database.entities.publisher.query;

import java.sql.Connection;
import java.sql.SQLException;

import lombok.Setter;
import server.database.Query;

public class AddPublisherPhone extends Query {
	
	private static final String ADD_PUBLISHER_PHONE = "INSERT INTO %s(ID, PHONE) VALUES(?, ?)";
	private static final String PHONE_TABLE = "PUBLISHER_PHONE";
	
	private static final int ID_IND = 1;
	private static final int PHONE_IND = 2;
	
	@Setter private int id;
	@Setter private String number;

	@Override
	protected void prepareStatement(Connection connection) throws SQLException {
		String query = String.format(ADD_PUBLISHER_PHONE, PHONE_TABLE);
		ps = connection.prepareStatement(query);
		ps.setInt(ID_IND, id);
		ps.setString(PHONE_IND, number);
	}

}
