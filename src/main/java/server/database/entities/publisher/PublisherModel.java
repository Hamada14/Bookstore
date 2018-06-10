package server.database.entities.publisher;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import server.ResponseData;
import server.database.entities.publisher.query.PublisherByID;
import server.database.entities.publisher.query.SelectAddressById;
import server.database.entities.publisher.query.SelectPhoneById;

public class PublisherModel {
	
	private static final String PUBLISHER_NOT_FOUND = "Publisher name not found";
	
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
	
	public static ResponseData addPublisherAddress(Publisher publisher, PublisherAddress address, Connection connection) {
		int id = publisher.getID(connection);
		ResponseData rs = new ResponseData();
		if(id == Publisher.PUBLISHER_NOT_FOUND) {
			rs.setError(PUBLISHER_NOT_FOUND);
			return rs;
		}
		address.setId(id);
		return address.addAddress(connection);
	}
	
	public static ResponseData deleteAddress(Publisher publisher, PublisherAddress address, Connection connection) {
		int id = publisher.getID(connection);
		ResponseData rs = new ResponseData();
		if(id == Publisher.PUBLISHER_NOT_FOUND) {
			rs.setError(PUBLISHER_NOT_FOUND);
			return rs;
		}
		address.setId(id);
		return address.deleteAddress(connection);
	}
	
	public static List<PublisherAddress> getAddresses(Publisher publisher, Connection connection) {
		int id = publisher.getID(connection);
		if(id == Publisher.PUBLISHER_NOT_FOUND) {
			return new ArrayList<>();
		}
		SelectAddressById query = new SelectAddressById();
		try {
			query.setId(id);
			query.executeQuery(connection);
			ResultSet rs = query.getResultSet();
			List<PublisherAddress> addresses = new ArrayList<>();
			while(rs != null && rs.next()) {
				addresses.add(new PublisherAddress(rs.getString(1), rs.getString(2), rs.getString(3)));
			}
			return addresses;
		} catch(SQLException e) {
			return new ArrayList<>();
		}
	}
	
	public static ResponseData addPublisherPhone(Publisher publisher, PublisherPhone phone, Connection connection) {
		int id = publisher.getID(connection);
		ResponseData rs = new ResponseData();
		if(id == Publisher.PUBLISHER_NOT_FOUND) {
			rs.setError(PUBLISHER_NOT_FOUND);
			return rs;
		}
		phone.setId(id);
		return phone.addPhone(connection);
	}
	
	public static ResponseData deletePhone(Publisher publisher, PublisherPhone phone, Connection connection) {
		int id = publisher.getID(connection);
		ResponseData rs = new ResponseData();
		if(id == Publisher.PUBLISHER_NOT_FOUND) {
			rs.setError(PUBLISHER_NOT_FOUND);
			return rs;
		}
		phone.setId(id);
		return phone.deletePhone(connection);
	}
	
	public static List<PublisherPhone> getPhones(Publisher publisher, Connection connection) {
		int id = publisher.getID(connection);
		if(id == Publisher.PUBLISHER_NOT_FOUND) {
			return new ArrayList<>();
		}
		SelectPhoneById query = new SelectPhoneById();
		try {
			query.setId(id);
			query.executeQuery(connection);
			ResultSet rs = query.getResultSet();
			List<PublisherPhone> phones = new ArrayList<>();
			while(rs != null && rs.next()) {
				phones.add(new PublisherPhone(rs.getString(1)));
			}
			return phones;
		} catch (SQLException e) {
			return new ArrayList<>();
		}
	}
}
