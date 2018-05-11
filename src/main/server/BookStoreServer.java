package server;

import java.util.ArrayList;
import java.util.HashMap;

import javax.jws.WebMethod;


import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import entities.Book;
import entities.User;


@WebService
@SOAPBinding(style =  Style.DOCUMENT) 
public interface BookStoreServer {
	
	@WebMethod 
	boolean addNewUser(User newUser);
	
	@WebMethod
	boolean editUser(User modifiedUser);
	
    @WebMethod
    ArrayList<Book> searchBook (String filter, String valueFilter);
	
    @WebMethod 
	boolean addNewBook(Book newBook);
    
    @WebMethod
    boolean editBook(Book modifiedBook);
      
    @WebMethod
    boolean promoteUser(HashMap<String, User> temp);

}
