package view;

import lombok.Getter;


import lombok.Setter;
import server.database.entities.book.Book;

@Getter
@Setter
public class Parameters {
	
    private Book book;
    private Boolean editOrBuyMode;
    
    public Parameters() {
    }
    
}
