package server;

import lombok.Getter;
import lombok.Setter;
import server.database.entities.user.User;

@Getter
@Setter
public class UserResponseData extends ResponseData{	
		private User user;
	
}
