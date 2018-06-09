package server.errors;

public enum BookError {
	
	IVALID_BOOK_ISBN("ERROR: invalid book isbn"),
	INVALID_PUBLICATION_YEAR("ERROR: invalid publication year"),
	INVALID_SELLING_PRICE("ERROR: invalid selling price"),
	INVALID_BOOK_CATEGOTY("ERROR: invalid category"),
	INVALID_MIN_THRESHOLD("ERROR: invalid minimum threshold amount"),
	INVALID_QUANTITY("ERROR: invalid quantity"),
	INVALID_PUBLISHER_NAME("ERROR: invalid publisher name"),
	INVALID_AUTHOR_NAME("ERROR: invalid author name");
	
	private final String text;

	BookError(final String text) {
		this.text = text;
	}

	public String toString() {
		return text;
	}
}
