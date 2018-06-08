package server.database.entities.user.query;

import java.sql.Connection;
import java.sql.SQLException;

import lombok.Setter;
import server.database.Query;
import server.database.entities.user.User;
import server.database.entities.user.UserModel;

public class NewUser extends Query {

	private static final String NEW_USER_QUERY = "Insert into %s(USER_NAME, EMAIL, FIRST_NAME, LAST_NAME,  PASSWORD_SHA1, PHONE_NUMBER, STREET, CITY, COUNTRY)"
			+ "VALUES (?, ?, ?, ?, SHA1(?), ?, ?, ?, ?);";
	
	private static final int USER_NAME_INDEX = 1;
	private static final int EMAIL_INDEX = 2;
	private static final int FIRST_NAME_INDEX = 3;
	private static final int LAST_NAME_INDEX = 4;
	private static final int PASSWORD_INDEX = 5;
	private static final int PHONE_NUMBER_INDEX = 6;
	private static final int STREET_INDEX = 7;
	private static final int CITY_INDEX = 8;
	private static final int COUNTRY_INDEX = 9;
	
	@Setter private User user;
	
	@Override
	protected void prepareStatement(Connection connection) throws SQLException {
		String query = String.format(NEW_USER_QUERY, UserModel.USER_TABLE);
		ps = connection.prepareStatement(query);
		ps.setString(USER_NAME_INDEX, user.getIdentity().getUserName());
		ps.setString(FIRST_NAME_INDEX, user.getFirstName());
		ps.setString(LAST_NAME_INDEX, user.getLastName());
		ps.setString(EMAIL_INDEX, user.getEmail());
		ps.setString(PASSWORD_INDEX, user.getIdentity().getPassword());
		ps.setString(PHONE_NUMBER_INDEX, user.getPhoneNumber());
		ps.setString(STREET_INDEX, user.getStreet());
		ps.setString(CITY_INDEX, user.getCity());
		ps.setString(COUNTRY_INDEX, user.getCountry());
	}
}
