package server.database.entities.publisher;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import server.database.entities.Person;
import server.database.entities.publisher.query.AddPublisher;
import server.database.entities.publisher.query.PublisherByName;

public class Publisher extends Person {
		
	public static final int ERROR_PUBLISHER_ADDITION = -1;
	public static final int PUBLISHER_NOT_FOUND = -2;
	
	public Publisher(String publisherName) {
		super(publisherName);
	}
	
	public Publisher() {
		super();
	}
	
	public boolean isValid() {
		return validateName(name);
	}
	
	public int getID(Connection connection) {
		PublisherByName query = new PublisherByName();
		try {
			query.setName(getName());
			query.executeQuery(connection);
			ResultSet rs = query.getResultSet();
			int id = PUBLISHER_NOT_FOUND;
			while (rs.next()) {
				id = rs.getInt(ID_COL);
			}
			return id;
		} catch (SQLException e) {
			return PUBLISHER_NOT_FOUND;
		} finally {
			query.close();
		}
	}
	
	public int addPublisher(Connection connection) {
		if (!isValid()) {
			return ERROR_PUBLISHER_ADDITION;
		}
		AddPublisher query = new AddPublisher();
		try {
			query.setName(getName());
			query.executeQuery(connection);
			int rowsAffected = query.getUpdateCount();
			if(rowsAffected == 0) {
				throw new SQLException();
			}
			try (ResultSet generatedKeys = query.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					return generatedKeys.getInt(1);
				} else {
					throw new SQLException();
				}
			}
		} catch (SQLException e) {
			return ERROR_PUBLISHER_ADDITION;
		} finally {
			query.close();
		}
	}
}
