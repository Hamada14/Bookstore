package server;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import javax.jws.WebService;

import net.sf.jasperreports.engine.JRException;

import server.database.entities.user.Identity;
import server.database.entities.Order;
import server.database.entities.ShoppingCart;
import server.database.entities.author.Author;
import server.database.entities.author.AuthorModel;
import server.database.entities.book.Book;
import server.database.entities.book.BookBuilder;
import server.database.entities.book.BookModel;
import server.database.entities.publisher.Publisher;
import server.database.entities.publisher.PublisherModel;
import server.database.entities.user.UserBuilder;
import server.database.entities.user.UserModel;
import server.database.report.JasperReportCreator;
import server.database.report.ReportType;
import server.errors.BookError;

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
		return UserModel.addNewUser(userBuilder.buildUser(), connection);
	}

	@Override
	public UserResponseData loginUser(Identity identity) {
		return identity.isUser(connection);
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
		} catch (IOException e) {
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
		return UserModel.editPersonalInformation(userBuilder.buildUser(), connection);
	
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
	public BooksResponseData searchBook(Identity identity, int offset, int limit, Book book) {
		BooksResponseData booksResponse = new BooksResponseData();
		UserResponseData validUser = identity.isUser(connection);
		if (validUser.isSuccessful()) {
			BooksResponseData booksResponse2 = BookModel.searchBooks(book, offset, limit, connection);//shrouk part
//			System.out.println("in impl" + booksResponse2.getBooks().size());
			return booksResponse2;
		} else {
			booksResponse.setError(validUser.getError());
			return booksResponse;
		}
	}

	@Override
	public ResponseData addNewBook(BookBuilder newBookBuilder) {
		int publisherId = PublisherModel.addPublisher(newBookBuilder.getPublisher(), connection);
		if (publisherId == server.database.entities.publisher.Publisher.ERROR_PUBLISHER_ADDITION) {
			ResponseData responseData = new ResponseData();
			responseData.setError(BookError.INVALID_PUBLISHER_NAME.toString());
			return responseData;
		}
		return BookModel.addBook(newBookBuilder, connection);
	}

	@Override
	public ResponseData editBook(BookBuilder modifiedBook) {
		int publisherId = PublisherModel.addPublisher(modifiedBook.getPublisher(), connection);
		if (publisherId == Publisher.ERROR_PUBLISHER_ADDITION) {
			ResponseData responseData = new ResponseData();
			responseData.setError(BookError.INVALID_PUBLISHER_NAME.toString());
			return responseData;
		}
		return BookModel.editBook(modifiedBook, connection);
	}

	@Override
	public boolean placeOrder(String isbn, String quantity) {
		int q = 0;
		try {
			q = Integer.valueOf(quantity);
		} catch (NumberFormatException e) {
			return false;
		}
		Book book = BookModel.getBookByISBN(isbn, connection);
		return Order.addNewOrder(new Order(q, book), connection);
	}
	

	@Override
	public boolean promoteUser(String userName) {
		return UserModel.promoteUser(userName, connection);
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

	public OrderResponseData checkoutShoppingCart(Identity identity, ShoppingCart cart) {
		OrderResponseData rs = new OrderResponseData();
		UserResponseData validUser = identity.isUser(connection);
		if (validUser.isSuccessful()) {
			return cart.checkOut(identity.getUserName(), connection);
		} else {
			rs.setError(validUser.getError());
			return rs;
		}
	}

	@Override
	public List<String> getCategories() {
		return BookModel.getCategories(connection);
	}

	@Override
	public List<Author> getBookAuthors(String bookISBN) {
		List<Author> bookAuthors = AuthorModel.selectAuthorNameByISBN(bookISBN, connection);
		return bookAuthors;
	}
}
