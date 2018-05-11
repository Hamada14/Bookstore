package server;



import javax.xml.ws.Endpoint;
public class Publisher {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Endpoint.publish("http://localhost:9996/server/bookStore", new BookStoreServerImpl());
	
	}

}
