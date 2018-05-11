package server;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashMap;

import javax.jws.WebService;

import entities.Book;
import entities.User;

//Service Implementation
@WebService(endpointInterface = "server.BookStoreServer")

public class BookStoreServerImpl implements BookStoreServer{
 
	private Connection connection;
	
	public BookStoreServerImpl () {
		connection = connectToDatabase();
		
		
	};
	
	@Override
	public boolean addNewUser(User user) {
	   
		return false;
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
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con =  DriverManager.getConnection(  
			"jdbc:mysql://localhost:3306/AlphaBit","Customer","1406"); 
			return con;
		}
		catch (Exception e) { System.out.println(e);
		}
		return null; 	
	}


	@Override
	public boolean promoteUser(HashMap<String, User> temp) {
		// TODO Auto-generated method stub
		return false;
	}
}
