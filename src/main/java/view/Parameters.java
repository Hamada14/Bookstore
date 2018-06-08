package view;

import lombok.Getter;
import lombok.Setter;
import server.database.entities.Book;

@Getter
@Setter
public class Parameters {
    private Book book;
    private boolean firstTimeRegistered;
    
    public Parameters() {
    }
    
}
