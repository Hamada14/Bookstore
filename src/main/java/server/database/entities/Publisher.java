package server.database.entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Publisher extends Person {
	
	public static final int ERROR_PUBLISHER_ADDITION = -1;
	private static final int PUBLISHER_NOT_FOUND = -2;
	private static final String ADD_PUBLISHSER = "INSERT INTO %s (NAME) VALUES (?)";
	private static final String SELECT_WITH_COND = "SELECT FROM %s WHERE NAME = ?";
	private static final String PUBLISHER_TABLE = "PUBLISHER";
	private static final int PUBLISHER_FIRST_NAME = 1;
	
	public Publisher(String publisherName) {
		super(publisherName);
	}
	
	public Publisher() {
		super();
	}
	
	public static int addPublisher(Publisher publisher, Connection connection) {
		int publisherId = selectPublisher(publisher, connection);
		if (publisherId == PUBLISHER_NOT_FOUND) {
			publisherId = publisherAddition(publisher, connection);
			return publisherId;
		} else {
			return publisherId;
		}
	}
	
	private static int selectPublisher(Publisher publisher, Connection connection) {
		try {
			String query = String.format(SELECT_WITH_COND, PUBLISHER_TABLE);
			PreparedStatement st = (PreparedStatement) connection.prepareStatement(query);
			st.setString(PUBLISHER_FIRST_NAME, publisher.getName());
			ResultSet rs = st.executeQuery();
			int id = -1;
			while (rs.next()) {
				id = rs.getInt(ID_COL);
			}
			return id;
		} catch (SQLException e) {
			return PUBLISHER_NOT_FOUND;
		}
	}
	
	private static int publisherAddition(Publisher publisher, Connection connection) {
		boolean validNames = validateName(publisher.getName());
		if (!validNames) {
			return ERROR_PUBLISHER_ADDITION;
		}
		try {
			String query = String.format(ADD_PUBLISHSER, PUBLISHER_TABLE);
			PreparedStatement st = (PreparedStatement) connection.prepareStatement(query,
					Statement.RETURN_GENERATED_KEYS);
			st.setString(PUBLISHER_FIRST_NAME, publisher.getName());
			int rowsAffected = st.executeUpdate();
			if(rowsAffected == 0) {
				throw new SQLException();
			}
			try (ResultSet generatedKeys = st.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					return generatedKeys.getInt(1);
				} else {
					throw new SQLException();
				}
			}
		} catch (SQLException e) {
			return ERROR_PUBLISHER_ADDITION;
		}
	}
	
}
