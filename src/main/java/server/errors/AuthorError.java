package server.errors;

public enum AuthorError {
	AUTHOR_NOT_FOUND("Missing Author Name"),
	AUTHOR_NAME_INVALID("Invalid Author name"),
	INVALID_AUTHOR_REFERENCE("Author didn't write this book");
	
	private String text;

	AuthorError(String text) {
		this.text = text;
	}
	
	public String toString() {
		return text;
	}
}
