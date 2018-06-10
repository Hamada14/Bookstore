package server;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import javax.jws.WebService;

import net.sf.jasperreports.engine.JRException;

import server.database.entities.user.Identity;
import server.database.entities.Order;

import server.database.entities.shoppingcart.ShoppingCart;
import server.database.entities.shoppingcart.ShoppingCartModel;
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
import server.errors.AuthorError;
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
	public BooksResponseData advancedSearchBooks(Identity identity, int offset, int limit, Book book) {
		BooksResponseData booksResponse = new BooksResponseData();
		UserResponseData validUser = identity.isUser(connection);
		if (validUser.isSuccessful()) {
			BooksResponseData booksResponse2 = BookModel.searchAdvancedBooks(book, offset, limit, connection);//shrouk part

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
	public ResponseData placeOrder(String isbn, String quantity) {
		int q = 0;
		try {
			q = Integer.valueOf(quantity);
		} catch (NumberFormatException e) {
			ResponseData rs = new ResponseData();
			rs.setError(BookError.INVALID_NUMBERS.toString());
			return rs;
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
	public ResponseData deleteOrder(int orderId) {
		return Order.deleteOrderById(orderId, connection);
	}
	@Override

	public OrderResponseData checkoutShoppingCart(Identity identity, ShoppingCart cart) {
		OrderResponseData rs = new OrderResponseData();
		UserResponseData validUser = identity.isUser(connection);
		if (validUser.isSuccessful()) {
			return ShoppingCartModel.checkOut(cart,identity.getUserName(), connection);
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

	@Override
	public ResponseData addAuthor(Author author, String isbn) {
		int id = AuthorModel.addAuthor(author, connection);
		ResponseData response = new ResponseData();
		if(id == Author.ERROR_AUTHOR_ADDITION) {
			response.setError(BookError.INVALID_AUTHOR_NAME.toString());
			return response;
		}
		boolean authorRefAddition = BookModel.authorRefAddition(isbn, id, connection);
		if(!authorRefAddition) {
			response.setError(BookError.INVALID_BOOK_ISBN.toString());
		}
		return response;
	}
	
	public ResponseData deleteAuthorReference(String usedIsbn, Author author) {
		int authorId = author.getID(connection);
		if(authorId == Author.AUTHOR_NOT_FOUND) {
			ResponseData rs = new ResponseData();
			rs.setError(AuthorError.AUTHOR_NOT_FOUND.toString());
			return rs;
		}
		boolean successful = BookModel.deleteAuthorReference(usedIsbn, authorId, connection);
		if(!successful) {
			ResponseData rs = new ResponseData();
			rs.setError(AuthorError.INVALID_AUTHOR_REFERENCE.toString());
			return rs;
		}
		return new ResponseData();
	}
}
