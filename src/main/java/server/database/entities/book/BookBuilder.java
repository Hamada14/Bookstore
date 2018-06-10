package server.database.entities.book;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import server.ResponseData;
import server.database.entities.publisher.Publisher;
import server.errors.BookError;

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
		if(validateBookAttributes().isSuccessful()) {
			return new Book(normalizeISBN(bookISBN), bookTitle, publicationYear, sellingPrice, category, publisher, quantity, minimumThreshold);
		}
		return null;
	}
	
	public ResponseData validateBookAttributes() {
		List<String> errors = new ArrayList<>(); 
		errors.add(isValidISBN(bookISBN));
		errors.add(isValidPublicationYear(publicationYear));
		errors.add(isValidSellingPrice(sellingPrice));
		errors.add(isValidMinimumThreshold(minimumThreshold));
		String errorMsg = errors.stream().filter(error -> error != null).map(error -> "*" + error).
				collect(Collectors.joining(System.lineSeparator()));
		ResponseData response = new ResponseData();
		if(!errorMsg.isEmpty()) {
			response.setError(errorMsg);
		}
		return response;
	}

	private String isValidMinimumThreshold(int val) {
		boolean valid = val >= 0;
		if(!valid) {
			return BookError.INVALID_MIN_THRESHOLD.toString();
		} 
		return null;
	}

	public static String isValidSellingPrice(float value) {
		boolean valid =  value >= MIN_SELLING_PRICE && value <= MAX_SELLING_PRICE;
		if(!valid) {
			return BookError.INVALID_SELLING_PRICE.toString();
		}
		return null;
	}

	public static String isValidPublicationYear(String strValue) {
		int value;
		try {
			value = Integer.valueOf(strValue);
		} catch (NumberFormatException e) {
			return BookError.INVALID_PUBLICATION_YEAR.toString();
		}
		Calendar now = Calendar.getInstance();
		boolean valid = value >= 0 && value <= now.get(Calendar.YEAR);
		if(!valid) {
			return BookError.INVALID_PUBLICATION_YEAR.toString();
		}
		return null;
	}

	public static String normalizeISBN(String isbnInput) {
		String numericISBN = getNumericISBN(isbnInput);
		if(numericISBN.length() == 13) {
			return numericISBN;
		}
		return ISBN10toISBN13(numericISBN);
	}
	
	private static String getNumericISBN(String isbnInput) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < isbnInput.length(); i++) {
			if(Character.isDigit(isbnInput.charAt(i))) {
				sb.append(isbnInput.charAt(i));
			}
		}
		return sb.toString();
	}
	
	private String isValidISBN(String isbn) {
		if(isValidISBNFormat(isbn) && isValidISBNValue(isbn)) {
			return null;
		}
		return BookError.INVALID_BOOK_ISBN.toString();
	}
	
	private boolean isValidISBNFormat(String isbn) {
		Pattern pattern = Pattern.compile(ISBN_REGEX);
		Matcher matcher = pattern.matcher(isbn);
		if(matcher.matches()) {
			return true;
		}
		return false;
	}
	
	private boolean isValidISBNValue(String malformedIsbn) {
		String realIsbn = getNumericISBN(malformedIsbn);
		if(realIsbn.length() == 13) {
			return isValidISBN13Value(realIsbn);
		} else if(realIsbn.length() == 10) {
			return isValidISBN10Value(realIsbn);
		}
		return false;
	}
	
	private boolean isValidISBN10Value(String isbn) {
		Long isbnValue = Long.valueOf(isbn);
		int result = 0;
		int pos = 1;
		while(pos <= 10) {
			result += pos * (isbnValue % 10);
			isbnValue /= 10;
			pos++;
		}
		return (result % 11) == 0;
	}
	
	private boolean isValidISBN13Value(String isbn) {
		Long isbnValue = Long.valueOf(isbn);
		int result = 0;
		int pos = 1;
		while(pos <= 13) {
			result += (pos % 2 == 0 ? 3 : 1) * (isbnValue % 10);
			isbnValue /= 10;
			pos++;
		}
		return (result % 10) == 0;
	}
	
	public static String ISBN10toISBN13(String ISBN10) {
		String ISBN13 = ISBN10;
		ISBN13 = "978" + ISBN13.substring(0, 9);
		int d;

		int sum = 0;
		for (int i = 0; i < ISBN13.length(); i++) {
			d = ((i % 2 == 0) ? 1 : 3);
			sum += ((((int) ISBN13.charAt(i)) - '0') * d);
		}
		sum = (10 - (sum % 10)) % 10;
		ISBN13 = ISBN13 + Integer.toString(sum);

		return ISBN13;
	}
}
