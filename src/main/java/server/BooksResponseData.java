package server;

import java.util.ArrayList;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import server.database.entities.book.Book;

@Getter
@Setter
public class BooksResponseData extends ResponseData{
 private List<Book> books;
// String ind;
 
 	public BooksResponseData(){
 		books = new ArrayList<Book>();
 	
 	}
 	
 	public void addBook(Book book) {
 		books.add(book);
 	}
}
