package server.database.entities.publisher.query;

import java.sql.Connection;
import java.sql.SQLException;

import lombok.Setter;
import server.database.Query;

public class AddPublisherAddress extends Query {

	private static String INSERT_ADDRESS = "INSERT INTO %s(ID, STREET, CITY, COUNTRY) VALUES (?, ?, ?, ?)";
	private static String ADDRESS_TABLE = "PUBLISHER_ADDRESS";
	private static int ID_IND = 1;
	private static int STREET_IND = 2;
	private static int CITY_IND = 3;
	private static int COUNTRY_IND = 4;
	
	@Setter private int id;
	@Setter private String street;
	@Setter private String city;
	@Setter private String country;
	
	@Override
	protected void prepareStatement(Connection connection) throws SQLException {
		String query = String.format(INSERT_ADDRESS, ADDRESS_TABLE);
		ps = connection.prepareStatement(query);
		ps.setInt(ID_IND, id);
		ps.setString(STREET_IND, street);
		ps.setString(CITY_IND, city);
		ps.setString(COUNTRY_IND, country);
	}

}
