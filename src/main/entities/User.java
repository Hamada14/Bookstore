package entities;

import java.io.Serializable;

public class User implements  Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6108052518548327564L;


	public User(String userName, String firstName, String lastName, String email, String password, String address,
			String phoneNumber, boolean isManager) {
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.isManager = isManager;
	}
 
	public User() {}


	private String userName;
	private String firstName;
	private String lastName;
    private String email;
    private String password;
    private String address;
    private String phoneNumber;
    private boolean isManager;
    
    
	public boolean isManager() {
		return isManager;
	}



	public void setManager(boolean isManager) {
		this.isManager = isManager;
	}

	public String getUserName() {
		return userName;
	}



	public void setUserName(String userName) {
		this.userName = userName;
	}



	public String getFirstName() {
		return firstName;
	}



	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}



	public String getLastName() {
		return lastName;
	}



	public void setLastName(String lastName) {
		this.lastName = lastName;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	public String getAddress() {
		return address;
	}



	public void setAddress(String address) {
		this.address = address;
	}



	public String getPhoneNumber() {
		return phoneNumber;
	}



	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}



	@Override
	public String toString() {
		return "User [userName=" + userName + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", password=" + password + ", address=" + address + ", phoneNumber=" + phoneNumber + ", isManager="
				+ isManager + "]";
	}



	
  
   
}
