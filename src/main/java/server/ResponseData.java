package server;

public class ResponseData {

	private boolean isSuccess;
	private String error;
	
	public ResponseData() {
		this.isSuccess = true;
	}
	
	public void setError(String error) {
		this.isSuccess = false;
		this.error = error;
	}
	
	public String getError() {
		return error;
	}

	public boolean isSuccessful() {
		return isSuccess;
	}
}
