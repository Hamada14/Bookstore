package view;

import javafx.scene.control.Hyperlink;
import server.database.entities.book.Book;

public class BookHyperLink extends Hyperlink{
    private Book book;
    
    public BookHyperLink(Book book) {
    	this.book = book;
    }
    
    public Book getBook() {
    	return book;
    }
}
