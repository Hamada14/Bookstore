package server;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import javax.jws.WebService;

import net.sf.jasperreports.engine.JRException;
import server.database.entities.Author;
import server.database.entities.Book;
import server.database.entities.Identity;
import server.database.entities.Order;
import server.database.entities.User;
import server.database.entities.UserBuilder;
import server.database.report.JasperReportCreator;
import server.database.report.ReportType;

//Service Implementation
@WebService(endpointInterface = "server.BookStoreServer")
public class BookStoreServerImpl implements BookStoreServer {

	private final Connection connection;
	private final JasperReportCreator jasperReporter;

	public BookStoreServerImpl(Connection connection, JasperReportCreator jasperReporter) {
		this.connection = connection;
		this.jasperReporter = jasperReporter;
	};

	@Override
	public ResponseData addNewUser(UserBuilder userBuilder) {
		String errors = userBuilder.validateData();
		if (errors != null) {
			ResponseData rs = new ResponseData();
			rs.setError(errors);
			return rs;
		}
		return User.addNewUser(userBuilder.buildUser(), connection);
	}

	@Override
	public UserResponseData loginUser(Identity identity) {
		return identity.isValidIdentity(connection);
	}

	@Override
	public boolean isManager(Identity identity) {
		return identity.isManager(connection);
	}

	@Override
	public byte[] generateReport(Identity identity, ReportType reportType) {
		try {
			if (identity.isManager(connection)) {
				return jasperReporter.generateReport(reportType);
			}
		} catch (JRException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	public UserResponseData editUserInformation(UserBuilder userBuilder) {
		String errors = userBuilder.validatePersonalInformation();
		UserResponseData rs = new UserResponseData();
		if (errors != null) {
			rs.setError(errors);
			return rs;
		}
		return User.editPersonalInformation(userBuilder.buildUser(), connection);
	
	}

	@Override
	public ResponseData editUserIdentity(Identity identity, String newPassword) {
		UserBuilder builder = new UserBuilder();
		builder.setPassword(newPassword);
		String errors = builder.validateNewPassword();
		ResponseData rs = new ResponseData();
		if (errors != null) {
			rs.setError(errors);
			return rs;
		}
		return identity.editUserIdentity(newPassword, connection);
	}

	@Override
	public ArrayList<Book> searchBook(String filter, String valueFilter) {
	 return null;
	}

	@Override
	public boolean addNewBook(Book newBook, Author author, server.database.entities.Publisher publisher) {
		int authorId = Author.addAuthor(author, connection);
		int publisherId = server.database.entities.Publisher.addPublisher(publisher, connection);
		if (authorId == Author.ERROR_AUTHOR_ADDITION
				|| publisherId == server.database.entities.Publisher.ERROR_PUBLISHER_ADDITION) {
			return false;
		}
		newBook.setPublisherId(publisherId);
		return Book.addBook(newBook, authorId, connection);
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
