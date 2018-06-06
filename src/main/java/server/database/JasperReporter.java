package server.database;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class JasperReporter {
	
	public final static String TOP_TEN_BOOKS_REPORT = "jasperReports/TopTenBooks.jrxml";
	public final static String TOP_FIVE_CUSTOMERS = "jasperReports/TopFiveUsers.jrxml";
	public final static String SALES = "jasperReports/Sales.jrxml";
	
	private final Connection connection;
	
	public JasperReporter(final Connection connection) {
		this.connection = connection;
	}
	
	public byte[] generateReport(String reportType) throws JRException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		JasperReport report = JasperCompileManager.compileReport(reportType);
		Map<String, Object> parameters = new HashMap<String, Object>();
		JasperPrint print = JasperFillManager.fillReport(report, parameters, connection);
		JasperExportManager.exportReportToPdfStream(print, out);
		return out.toByteArray();
	}
}
