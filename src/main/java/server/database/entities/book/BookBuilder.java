package server.database.entities.book;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Getter;
import lombok.Setter;
import server.database.entities.publisher.Publisher;

@Setter @Getter
public class BookBuilder {

	private static final String ISBN_REGEX = "^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$";

	private static final float MAX_SELLING_PRICE = 999999.99f;
	private static final float MIN_SELLING_PRICE = 0.00f;
	
	private String bookISBN;
	private String bookTitle;
	private String publicationYear;
	private float sellingPrice;
	private String category;
	private Publisher publisher;
	private int quantity;
	private int minimumThreshold;
	
	public Book buildBook() {
		if(validateBookAttributes()) {
			return new Book(bookISBN, bookTitle, publicationYear, sellingPrice, category, publisher, quantity, minimumThreshold);
		}
		return null;
	}
	
	private boolean validateBookAttributes() {
		return isValidISBN(bookISBN) && isValidPublicationYear(publicationYear)
				&& isValidSellingPrice(sellingPrice) && isValidMinimumThreshold(minimumThreshold);
	}

	private static boolean isValidMinimumThreshold(int val) {
		return val >= 0;
	}

	private static boolean isValidSellingPrice(float value) {
		return value >= MIN_SELLING_PRICE && value <= MAX_SELLING_PRICE;
	}

	private static boolean isValidPublicationYear(String strValue) {
		int value;
		try {
			value = Integer.valueOf(strValue);
		} catch (NumberFormatException e) {
			return false;
		}
		Calendar now = Calendar.getInstance();
		return value >= 0 && value <= now.get(Calendar.YEAR);
	}

	private static boolean isValidISBN(String isbn) {
		Pattern pattern = Pattern.compile(ISBN_REGEX);
		Matcher matcher = pattern.matcher(isbn);
		return matcher.matches();
	}
}
