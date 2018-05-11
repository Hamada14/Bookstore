package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import javax.jws.WebService;

import server.database.DBConfig;
import server.database.entities.Book;
import server.database.entities.User;

//Service Implementation
@WebService(endpointInterface = "server.BookStoreServer")
public class BookStoreServerImpl implements BookStoreServer {

	private Connection connection;
	private DBConfig config;

	public BookStoreServerImpl(DBConfig config) {
		this.config = config;
		this.connection = connectToDatabase();
	};

	@Override
	public ResponseData addNewUser(User user) {
		String userErrors = user.validateData();
		if(userErrors != null) {
			ResponseData rs = new ResponseData();
			rs.setError(userErrors);
			return rs;
		}
		try {
			Statement st = connection.createStatement();
			st.execute("use " + config.getPropertyValue(DBConfig.DB_NAME));
			user.registerUser(connection, config.getPropertyValue(DBConfig.USER_TABLE_NAME));
		} catch (SQLException e) {
			ResponseData rs = new ResponseData();
			rs.setError("Couldn't execute query");
			e.printStackTrace();
			return rs;
		}
		return new ResponseData();
	}

	@Override
	public boolean editUser(User user) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ArrayList<Book> searchBook(String filter, String valueFilter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addNewBook(Book newBook) {
		return false;
	}

	@Override
	public boolean editBook(Book modifiedBook) {
		// TODO Auto-generated method stub
		return false;
	}

	private Connection connectToDatabase() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String dbName = config.getPropertyValue(DBConfig.DB_NAME);
			String userName = config.getPropertyValue(DBConfig.DB_USER_NAME);
			String password = config.getPropertyValue(DBConfig.DB_PASSWORD);

			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbName, userName, password);
			return con;
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}
	
	@Override
	public boolean promoteUser(HashMap<String, User> temp) {
		// TODO Auto-generated method stub
		return false;
	}
}
