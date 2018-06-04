package server;

import java.sql.Connection;

import java.util.ArrayList;
import java.util.HashMap;

import javax.jws.WebService;

import net.sf.jasperreports.engine.JRException;
import server.database.JasperReporter;
import server.database.entities.Book;
import server.database.entities.Identity;
import server.database.entities.User;

//Service Implementation
@WebService(endpointInterface = "server.BookStoreServer")
public class BookStoreServerImpl implements BookStoreServer {

	private final Connection connection;
	private final JasperReporter jasperReporter;

	public BookStoreServerImpl(Connection connection, JasperReporter jasperReporter) {
		this.connection = connection;
		this.jasperReporter = jasperReporter;
	};

	@Override
	public ResponseData addNewUser(User user) {
		return User.addNewUser(user, connection);
	}

	@Override
	public ResponseData loginUser(Identity identity) {
		return identity.isUser(connection);
	}

	@Override
	public boolean isManager(Identity identity) {
		return identity.isManager(connection);
	}

	@Override
	public byte[] generateReport(Identity identity, String reportType) {
		try {
			if (identity.isManager(connection)) {
				return jasperReporter.generateReport(reportType);
			}
		} catch (JRException e) {
			e.printStackTrace();
		}
		return null;
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

	@Override
	public boolean promoteUser(HashMap<String, User> temp) {
		// TODO Auto-generated method stub
		return false;
	}
}
