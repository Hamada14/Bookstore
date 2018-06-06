package view;

import server.database.entities.Book;
import server.database.entities.Identity;

public class Parameters {
    private Book book;
    private boolean firstTimeRegistered;
    
    public Parameters() {
    	
    }
    
    public void setRegisterationMode(boolean registerationMode) {
    	firstTimeRegistered = registerationMode;
    }
    
    public boolean getRegisterationMode() {
    	return firstTimeRegistered;
    }
    public void setBook (Book book) {
    	this.book = book;
    }
    
    public Book getBook() {
    	return book;
    }
    
}
