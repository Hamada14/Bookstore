package server.database;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DBConfig {

	public static final String DB_USER_NAME = "user";
	public static final String DB_PASSWORD = "password";
	public static final String DB_NAME = "db";
	
	private static final String PROP_FILE_NAME = "config.properties";
	
	private final Properties properties;

	public DBConfig() throws IOException {
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(PROP_FILE_NAME);
		this.properties = new Properties();
		properties.load(inputStream);
		
	}

	public String getPropertyValue(String property) {
		return properties.getProperty(property);
	}
}