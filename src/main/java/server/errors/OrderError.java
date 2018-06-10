package server.errors;

public enum OrderError {
	
	ERROR_DELETING_ORDER("ERROR: cannot delete order."),
	ERROR_MESSAGE_HEADER("Error loading orders"),
	SUCCESS_MESSAGE_HEADER("Success!"),
	ORDER_DELETED_SUCCESSFULLY("Order deleted successfully"),
	ERROR_ADDING_ORDER("ERROR: cannot place order.");
	
	private String text;
	
	OrderError(String text) {
		this.text = text;
	}
	
	public String toString() {
		return text;
	}
	
}
