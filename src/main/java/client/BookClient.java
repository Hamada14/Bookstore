package client;

import java.net.MalformedURLException;



import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import server.BookStoreServer;
import server.Publisher;

public class BookClient {

	private static BookStoreServer server;

	static {
		try {
			URL url = new URL(Publisher.endPointURL);
			QName qname = new QName("http://server/", "BookStoreServerImplService");
			Service service = Service.create(url, qname);
			server = service.getPort(BookStoreServer.class);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public static BookStoreServer getServer() {
		return server;
	}
}
