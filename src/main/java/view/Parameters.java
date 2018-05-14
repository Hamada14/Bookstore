package view;

import server.database.entities.Book;
import server.database.entities.Identity;

public class Parameters {
    Book book;
    Identity userIdentity;
    
    public void setBook (Book book) {
    	this.book = book;
    }
    
    public Book getBook() {
    	return book;
    }
    
}
