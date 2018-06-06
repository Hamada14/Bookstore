package server;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jws.WebService;


import server.database.DBConfig;
import server.database.entities.Book;
import server.database.entities.Identity;
import server.database.entities.Order;
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
		return User.addNewUser(user, connection);
	}

	@Override
	public ResponseData loginUser(Identity identity) {
		return identity.isValidIdentity(connection);
	}
	
	@Override
	public boolean isManager(Identity identity) {
		return identity.isManager(connection);
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
			Statement st = con.createStatement();
			st.execute("use " + config.getPropertyValue(DBConfig.DB_NAME));
			return con;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean promoteUser(HashMap<String, User> temp) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean placeOrder(String isbn, String quantity) {
		int q = 0;
		try {
			q = Integer.valueOf(quantity);
		} catch (NumberFormatException e) {
			return false;
		}
		Book book = new Book();
		book.setBookISBN(isbn);
		return Order.addNewOrder(new Order(q, book), connection);
	}
}
