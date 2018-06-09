package server.database.report;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class JasperReportCreator {
	
	private final Connection connection;
	
	public JasperReportCreator(final Connection connection) {
		this.connection = connection;
	}
	
	public byte[] generateReport(ReportType reportType) throws JRException, IOException {
		JasperReport report = JasperCompileManager.compileReport(reportType.getPath());
		Map<String, Object> parameters = new HashMap<String, Object>();
		JasperPrint print = JasperFillManager.fillReport(report, parameters, connection);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
		objectOutputStream.writeObject(print);
		return byteArrayOutputStream.toByteArray();
	}
}
