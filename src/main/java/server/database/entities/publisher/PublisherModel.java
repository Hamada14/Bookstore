package server.database.entities.publisher;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import server.database.entities.publisher.query.PublisherByID;

public class PublisherModel {
	public static final String PUBLISHER_TABLE = "PUBLISHER";
	
	public static final String PUBLIISHER_NAME_COL = "Name";
	
	public static int addPublisher(Publisher publisher, Connection connection) {
		int publisherId = publisher.getID(connection);
		if (publisherId == Publisher.PUBLISHER_NOT_FOUND) {
			publisherId = publisher.addPublisher(connection);
			return publisherId;
		} else {
			return publisherId;
		}
	}

	public static Publisher getPublisherByID(int id, Connection connection) {
		PublisherByID query = new PublisherByID();
		try {
			query.setId(id);
			query.executeQuery(connection);
			ResultSet rs = query.getResultSet();
			if(rs == null || !rs.next()) {
				return null;
			}
			return new Publisher(rs.getString(PUBLIISHER_NAME_COL));
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			query.close();
		}
	}
}
