package client;





import java.net.MalformedURLException;

import java.net.URL;
import java.util.HashMap;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import entities.User;
import server.BookStoreServer;






public class BookClient {

	public static void main(String[] args) {
		try {
			URL url = new URL("http://localhost:9996/server/bookStore?wsdl");
	      QName qname = new QName("http://server/", "BookStoreServerImplService");
	      Service service = Service.create(url, qname);
	      BookStoreServer server = service.getPort(BookStoreServer.class);
	      User user = new User("shosho", "Shrouk", "Ashraf", "shosho1996@ymail.com","1407", "alexo","0123456789",false);	      
	      System.out.println(server.addNewUser(user));
		} catch (MalformedURLException e) {
			
			e.printStackTrace();
		}




	}

}
