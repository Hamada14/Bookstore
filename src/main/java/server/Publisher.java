package server;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import javax.xml.ws.Endpoint;

import server.database.DBConfig;
import server.database.report.JasperReportCreator;

public class Publisher {


	public static final String endPointURL = "http://localhost:9996/server/bookStore";

	public static void main(String[] args) {
		try {
			DBConfig config = new DBConfig();
			Connection connection = Publisher.connectToDatabase(config);
			JasperReportCreator jasperReporter = new JasperReportCreator(connection);
			BookStoreServerImpl bookStoreServer = new BookStoreServerImpl(connection, jasperReporter);
			Endpoint.publish(endPointURL, bookStoreServer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Connection connectToDatabase(DBConfig config) {
		try {
			String dbName = config.getPropertyValue(DBConfig.DB_NAME);
			String userName = config.getPropertyValue(DBConfig.DB_USER_NAME);
			String password = config.getPropertyValue(DBConfig.DB_PASSWORD);

			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbName, userName, password);
			Statement st = con.createStatement();
			st.execute("use " + config.getPropertyValue(DBConfig.DB_NAME));
			return con;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
