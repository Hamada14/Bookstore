package server;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.jws.WebService;

import net.sf.jasperreports.engine.JRException;
import server.database.entities.Author;
import server.database.entities.Book;
import server.database.entities.Identity;
import server.database.entities.Order;
import server.database.entities.ShoppingCart;
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
	public BooksResponseData searchBook(Identity identity, String filter, String valueFilter) {
		BooksResponseData booksResponse = new BooksResponseData();
		UserResponseData validUser = identity.isValidIdentity(connection);
		if (validUser.isSuccessful()) {
			BooksResponseData booksResponse2 = Book.searchBooks(filter, valueFilter, connection);
//			System.out.println("in impl" + booksResponse2.getBooks().size());
			return booksResponse2;
		} else {
			booksResponse.setError(validUser.getError());
			return booksResponse;
		}
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
	

	@Override
	public boolean promoteUser(String userName) {
		return User.promoteUser(userName, connection);
	}
	
	@Override
	public List<Order> getOrders(int offset, int limit) {
		return Order.selectAllOrders(offset, limit, connection);
	}
	
	@Override
	public boolean deleteOrder(int orderId) {
		return Order.deleteOrderById(orderId, connection);
	}
	@Override
	public ResponseData checkoutShoppingCart(Identity identity, ShoppingCart cart) {
		ResponseData rs = new ResponseData();
		UserResponseData validUser = identity.isValidIdentity(connection);
		if (validUser.isSuccessful()) {
			return cart.checkOut(identity.getUserName(), connection);
		} else {
			rs.setError(validUser.getError());
			return rs;
		}
	}
}
