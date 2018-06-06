package server;

import server.database.entities.User;

public class UserResponseData extends ResponseData{
	
		private User user;
		
		public void setUser(User user) {
			this.user = user;
		}
		
		public User getUser() {
			return user;
		}
	
}
