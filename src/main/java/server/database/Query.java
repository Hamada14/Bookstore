package server.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Query {

	private boolean hasResultSet;
	private ResultSet resultSet;
	
	protected PreparedStatement ps;
	
	public void close() {
		try { ps.close(); } catch (SQLException e) { e.printStackTrace(); }
	}
	
	public boolean executeQuery(Connection connection) throws SQLException {
		prepareStatement(connection);
		hasResultSet = ps.execute();
		resultSet = ps.getResultSet();
		return hasResultSet;
	}
	
	public int getUpdateCount() throws SQLException {
		return ps.getUpdateCount();
	}
	
	public boolean hasResultSet() {
		return hasResultSet;
	}
	
	public ResultSet getResultSet() {
		return resultSet;
	}
	
	protected abstract void prepareStatement(Connection connection) throws SQLException ;
}
