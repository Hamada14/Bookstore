package view;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BookTuple {
	private BookHyperLink bookTitle;
	private BookHyperLink editLink;
	
	public BookTuple(BookHyperLink title) {
		this.bookTitle = title;
	}
}
