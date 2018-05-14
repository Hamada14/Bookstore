package server;

import java.io.IOException;


import javax.xml.ws.Endpoint;

import server.database.DBConfig;

public class Publisher {

	public static final String endPointURL = "http://localhost:9991/server/bookStore";
	
	public static void main(String[] args) {
		try {
			DBConfig config = new DBConfig();
			BookStoreServerImpl bookStoreServer= new BookStoreServerImpl(config);
			Endpoint.publish(endPointURL, bookStoreServer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
