package server.database.entities.publisher.query;

import java.sql.Connection;
import java.sql.SQLException;

import lombok.Setter;
import server.database.Query;

public class DeleteAddress extends Query {

	private static final String DELETE_ADDRESS = "DELETE FROM %s WHERE ID = ? AND STREET = ? AND CITY = ? AND COUNTRY = ?";
	private static final String ADDRESS_TABLE = "PUBLISHERS_ADDRESS";
	private static final int ID_IND = 1;
	private static final int STREET_IND = 2;
	private static final int CITY_IND = 3;
	private static final int COUNTRY_IND = 4;
	
	
	@Setter private int id;
	@Setter private String street;
	@Setter private String city;
	@Setter private String country;
	@Override
	protected void prepareStatement(Connection connection) throws SQLException {
		String query = String.format(DELETE_ADDRESS,  ADDRESS_TABLE);
		ps = connection.prepareStatement(query);
		ps.setInt(ID_IND, id);
		ps.setString(STREET_IND, street);
		ps.setString(CITY_IND, city);
		ps.setString(COUNTRY_IND, country);
	}

}
