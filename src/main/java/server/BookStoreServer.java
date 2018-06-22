package server;

import java.util.List;


import javax.jws.WebMethod;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import server.database.entities.user.Identity;
import server.database.entities.shoppingcart.ShoppingCart;
import server.database.entities.author.Author;
import server.database.entities.publisher.Publisher;
import server.database.entities.publisher.PublisherAddress;
import server.database.entities.publisher.PublisherPhone;
import server.database.entities.book.Book;
import server.database.entities.book.BookBuilder;
import server.database.entities.order.Order;
import server.UserResponseData;
import server.database.entities.user.UserBuilder;
import server.database.report.ReportType;


@WebService
@SOAPBinding(style = Style.DOCUMENT)
public interface BookStoreServer {

	@WebMethod
	ResponseData addNewUser(UserBuilder newUserBuilder);

	@WebMethod
	UserResponseData editUserInformation(UserBuilder modifiedUser);

	@WebMethod
	ResponseData editUserIdentity(Identity identity, String newPassword);

	@WebMethod
	BooksResponseData advancedSearchBooks(Identity identity, int offset, int limit, Book selectedBook);
	
	@WebMethod
	BooksResponseData simpleSearchBooks(Identity identity, int offset, int limit, String title);

	@WebMethod
	ResponseData addNewBook(Identity identity, BookBuilder newBook);

	@WebMethod
	ResponseData editBook(Identity identity, BookBuilder bookBuilder);
	
	@WebMethod
	UserResponseData loginUser(Identity identity);

	@WebMethod
	boolean isManager(Identity identity);
	
	@WebMethod
	ResponseData placeOrder(Identity identity, String isbn, String quantity);
	
	@WebMethod
	ResponseData promoteUser(Identity identity, String userName);

	@WebMethod
	List<Order> getOrders(Identity identity, int offset, int limit);
	
	@WebMethod
	ResponseData deleteOrder(Identity identity, int orderId);
	
	@WebMethod
	byte[] generateReport(Identity identity, ReportType reportType);

	@WebMethod
	OrderResponseData checkoutShoppingCart(Identity identity, ShoppingCart cart);
	
	@WebMethod
	List<Author> getBookAuthors(Identity identity, String bookISBN);

	@WebMethod
	List<String> getCategories();

	@WebMethod
	ResponseData addAuthor(Identity identity, Author author, String isbn);

	@WebMethod
	ResponseData deleteAuthorReference(Identity identity, String usedIsbn, Author author);
	
	@WebMethod
	ResponseData addPublisher(Identity identity, Publisher publisher);
	
	@WebMethod
	ResponseData addPublisherAddress(Identity identity, Publisher publisher, PublisherAddress publisherAddress);
	
	@WebMethod
	ResponseData deletePublisherAddress(Identity identity, Publisher publisher, PublisherAddress publisherAddress);
	
	@WebMethod 
	List<PublisherAddress> loadPublisherAddresses(Identity identity, Publisher publisher);
	
	@WebMethod
	ResponseData addPublisherPhone(Identity identity, Publisher publisher, PublisherPhone publisherPhone);
	
	@WebMethod
	ResponseData deletePublisherPhone(Identity identity, Publisher publisher, PublisherPhone publisherPhone);
	
	@WebMethod
	List<PublisherPhone> loadPublisherPhones(Identity identity, Publisher publisher);
	
}
