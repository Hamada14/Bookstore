package server.errors;

public enum SqlError {
	
	DUPLICATE_KEY("ERROR: repeated registration."),
	SERVER_ERROR("ERROR: there is an error in the server."),
	ENTRY_NOT_FOUND("ERROR: inavlid information."),
	
	ERROR_MESSAGE_TITLE("Error!");

	private final String text;

	SqlError(final String text) {
		this.text = text;
	}

	public String toString() {
		return text;
	}
}
