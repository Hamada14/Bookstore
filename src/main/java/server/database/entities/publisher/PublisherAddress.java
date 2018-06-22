package server.database.entities.publisher;

import java.sql.Connection;
import java.sql.SQLException;

import lombok.Getter;
import lombok.Setter;
import server.ResponseData;
import server.database.entities.publisher.query.AddPublisherAddress;
import server.database.entities.publisher.query.DeleteAddress;

@Setter
@Getter
public class PublisherAddress {
	
	private static final String ERROR_ADDRESS_ADDITION = "Cannot add address.";
	private static final String ERROR_ADDRESS_DELETION = "Error address deletion.";
	
	private String street;
	private String city;
	private String country;
	private int id;
	
	public PublisherAddress(String street, String city, String country) {
		this.city = city;
		this.street = street;
		this.country = country;
	}
	
	public PublisherAddress() {
		
	}
	
	public ResponseData addAddress(Connection connection) {
		ResponseData rs = new ResponseData();
		if(!validateAddressPart(street) || !validateAddressPart(city) || !validateAddressPart(country)) {
			rs.setError(ERROR_ADDRESS_ADDITION);
			return rs;
		}
		AddPublisherAddress query = new AddPublisherAddress();
		try {
			query.setCity(city);
			query.setCountry(country);
			query.setId(id);
			query.setStreet(street);
			System.out.println(query.toString());
			query.executeQuery(connection);
			int rowsAffected = query.getUpdateCount();
			if(rowsAffected == 0) {
				throw new SQLException();
			}
		} catch(SQLException e) {
			rs.setError(ERROR_ADDRESS_ADDITION);
		}
		return rs;
	}
	
	private boolean validateAddressPart(String part) {
		if(part == null || part.isEmpty()) {
			return false;
		}
		for(int i = 0; i < part.length(); i++) {
			if(part.charAt(i) == ',') {
				return false;
			}
		}
		return true;
	}
	
	public ResponseData deleteAddress(Connection connection) {
		DeleteAddress query = new DeleteAddress();
		ResponseData rs = new ResponseData();
		try {
			query.setCity(city);
			query.setCountry(country);
			query.setId(id);
			query.setStreet(street);
			query.executeQuery(connection);
			int rowsAffected = query.getUpdateCount();
			if(rowsAffected == 0) {
				throw new SQLException();
			}
		} catch (SQLException e) {
			rs.setError(ERROR_ADDRESS_DELETION);
		}
		return rs;
	}
	
}
