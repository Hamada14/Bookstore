package server.database.entities.publisher;

import java.sql.Connection;
import java.sql.SQLException;

import lombok.Getter;
import lombok.Setter;
import server.ResponseData;
import server.database.entities.publisher.query.AddPublisherPhone;
import server.database.entities.publisher.query.DeletePublisherPhone;

@Setter
@Getter
public class PublisherPhone {
	private static final String ERROR_PHONE_ADDITION = "Cannot add phone.";
	private static final String ERROR_PHONE_DELETION = "Error phone deletion.";
	
	private String number;
	private int id;
	
	public PublisherPhone(String number) {
		this.number = number;
	}
	
	public PublisherPhone() {
		
	}
	
	public ResponseData addPhone(Connection connection) {
		AddPublisherPhone query = new AddPublisherPhone();
		ResponseData rs = new ResponseData();
		try {
			query.setNumber(number);
			query.setId(id);
			System.out.println("HERE2");
			query.executeQuery(connection);
			System.out.println("HERE3");
			int rowsAffected = query.getUpdateCount();
			if(rowsAffected == 0) {
				throw new SQLException();
			}
		} catch(SQLException e) {
			rs.setError(ERROR_PHONE_ADDITION);
		}
		return rs;
	}
	
	public ResponseData deletePhone(Connection connection) {
		DeletePublisherPhone query = new DeletePublisherPhone();
		ResponseData rs = new ResponseData();
		try {
			query.setNumber(number);
			query.setId(id);
			query.executeQuery(connection);
			int rowsAffected = query.getUpdateCount();
			if(rowsAffected == 0) {
				throw new SQLException();
			}
		} catch (SQLException e) {
			rs.setError(ERROR_PHONE_DELETION);
		}
		return rs;
	}

}
