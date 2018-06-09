package server.database.report;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.swing.JFrame;

import client.alphabit.BookStoreApp;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Alert.AlertType;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JRViewer;
import server.BookStoreServer;

public class ReportViewer extends JFrame {

	private static final long serialVersionUID = -4207366179824990628L;

	private static final int WINDOW_WIDTH = 600;
	private static final int WINDOW_HEIGHT = 900;
	private static final String FAILED_WRITE_TITLE = "Error";
	private static final String FAILED_WRITE_HEADER = "Error Display report";
	private static final String FAILED_WRITE_CONTENT = "Please try again";

	private static final String REPORT_TYPE_TITLE = "Jasper Report";
	private static final String REPORT_TYPE_HEADER = "Specify report type";

	private final BookStoreServer server;

	public ReportViewer(BookStoreServer server) {
		this.server = server;
	}

	public void viewReport() {
		ReportType type = getUserReportPreference();
		if (type != null) {
			byte[] reportData = server.generateReport(BookStoreApp.getUser().getIdentity(), type);
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(reportData);
			try {
				ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
				JasperPrint jasperPrint = (JasperPrint) objectInputStream.readObject();
				JRViewer reportViewer = new JRViewer(jasperPrint);
				reportViewer.setVisible(true);
				this.add(reportViewer);
				this.setSize(WINDOW_HEIGHT, WINDOW_WIDTH);
				this.setVisible(true);
			} catch (ClassNotFoundException | IOException e) {
				BookStoreApp.displayDialog(AlertType.ERROR, FAILED_WRITE_TITLE, FAILED_WRITE_HEADER,
						FAILED_WRITE_CONTENT);
				e.printStackTrace();
			}
		}
	}

	private ReportType getUserReportPreference() {
		List<String> reportTypes = Arrays.stream(ReportType.values()).map(ReportType::getName)
				.collect(Collectors.toList());
		ChoiceDialog<String> dialog = new ChoiceDialog<>(reportTypes.get(0), reportTypes);
		dialog.setTitle(REPORT_TYPE_TITLE);
		dialog.setHeaderText(REPORT_TYPE_HEADER);
		Optional<String> choosenType = dialog.showAndWait();
		if (!choosenType.isPresent()) {
			return null;
		}
		return ReportType.valueByName(choosenType.get());
	}
}
