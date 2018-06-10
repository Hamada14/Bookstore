package server.database.entities.user;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import server.ResponseData;
import server.UserResponseData;
import server.database.entities.user.query.PromoteUser;
import server.database.entities.user.query.UserByUserName;

public class UserModel {

	public static final String USER_TABLE = "CUSTOMER";
	public static final String MANAGER_TABLE = "MANAGER";

	
	private static final String DUPLICATE_EMAIL_ERROR = "User with this email already registered";
	private static final String DUPLICATE_USER_NAME_ERROR = "User with this user name already registered";
	private static final String USER_NOT_FOUND = "User not found";
	private static final String ERROR_PROMOTING_USER = "Cannot promote user";

	public static List<String> getValidCountries() {
		Locale[] locales = Locale.getAvailableLocales();
		List<String> countries = new ArrayList<>();
		for (Locale locale : locales) {
			String country = locale.getDisplayCountry();
			if (country.trim().length() > 0 && !countries.contains(country)) {
				countries.add(country);
			}
		}
		Collections.sort(countries);
		return countries;
	}
	
	public static ResponseData addNewUser(User user, Connection connection) {
		ResponseData rs = new ResponseData();
		try {
			user.registerUser(connection);
		} catch (SQLException e) {
			try {
				List<String> errors = new ArrayList<>();
				if (user.isAlreadyRegisteredUserName(connection)) {
					errors.add(DUPLICATE_USER_NAME_ERROR);
				}
				if (user.isAlreadyRegisteredEmail(connection)) {
					errors.add(DUPLICATE_EMAIL_ERROR);
				}
				String combinedErrors = errors.stream().map(error -> "* " + error)
						.collect(Collectors.joining(System.lineSeparator()));
				rs.setError(combinedErrors);
			} catch (SQLException e1) {
				rs.setError("Please try again later");
			}
		}
		return rs;
	}
	
	public static UserResponseData editPersonalInformation(User modifiedUser, Connection connection) {
		UserResponseData rs = new UserResponseData();
		try {
			modifiedUser.updatePersonalInfo(connection);
			rs.setUser(modifiedUser);
		} catch (SQLException e) {
			rs.setError(e.getMessage());
		}
		return rs;
	}

	public static ResponseData promoteUser(String userName, Connection connection) {
		boolean validUserName = isExistingUserName(userName, connection);
		ResponseData rs = new ResponseData();
		if (!validUserName) {
			rs.setError(USER_NOT_FOUND);
			return rs;
		}
		PromoteUser query = new PromoteUser();
		try {
			query.setUserName(userName);
			query.executeQuery(connection);
			int rowsAffected = query.getUpdateCount();
			if(rowsAffected != 1) {
				throw new SQLException();
			}
			return rs;
		} catch (SQLException e) {
			rs.setError(ERROR_PROMOTING_USER);
			return rs;
 		} finally {
 			query.close();
 		}
	}

	private static boolean isExistingUserName(String userName, Connection connection) {
		UserByUserName query =  new UserByUserName();
		try {
			query.setUserName(userName);
			query.executeQuery(connection);
			ResultSet rs = query.getResultSet();
			boolean result = rs != null && rs.next();
			return result;
		} catch (SQLException e) {
			return false;
		} finally {
			query.close();		
		}
	}
}
