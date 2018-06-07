package server.database.report;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import client.alphabit.BookStoreApp;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import server.BookStoreServer;

public class ReportWriter {

	private static final String FAILED_WRITE_TITLE = "Error";
	private static final String FAILED_WRITE_HEADER = "Error writing report";
	private static final String FAILED_WRITE_CONTENT = "Please try again";
	
	private static final String REPORT_TYPE_TITLE = "Jasper Report";
	private static final String REPORT_TYPE_HEADER = "Specify report type";

	private static final String PDF_FILTER_MSG = "PDF Documents (*.pdf)";
	private static final String PDF_FILTER_FORMAT = "*.pdf";
	
	private final BookStoreServer server;
	
	public ReportWriter(BookStoreServer server) {
		this.server = server;
	}
	
	public void createReport() {
		ReportType type = getUserReportPreference();
		if(type != null) {
			Optional<String> path = getReportPath(type);
			path.ifPresent(pathValue -> {
				byte[] reportData = server.generateReport(BookStoreApp.getUser().getIdentity(), type);
				writeReportData(pathValue, reportData);
			});
		}
	}
	

	private ReportType getUserReportPreference() {
		List<String> reportTypes = Arrays.stream(ReportType.values())
				.map(ReportType::getName).collect(Collectors.toList());
		ChoiceDialog<String> dialog = new ChoiceDialog<>(reportTypes.get(0), reportTypes);
		dialog.setTitle(REPORT_TYPE_TITLE);
		dialog.setHeaderText(REPORT_TYPE_HEADER);
		Optional<String> choosenType = dialog.showAndWait();
		if(!choosenType.isPresent()) {
			return null;
		}
		return ReportType.valueByName(choosenType.get()); 
	}
	
	private Optional<String> getReportPath(final ReportType type) {
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter pdfFilter = new FileChooser.ExtensionFilter(PDF_FILTER_MSG, PDF_FILTER_FORMAT);
		fileChooser.setTitle(type.getName());
		fileChooser.getExtensionFilters().add(pdfFilter);
		fileChooser.setInitialFileName(type.getdefaultPDFFile());
		File file = fileChooser.showSaveDialog(null);
		return file == null ?  Optional.empty() : Optional.of(file.getAbsolutePath());
	}
	
	private void writeReportData(String path, byte[] reportData) {
	    try {
	    	Files.write(Paths.get(path), reportData);
		} catch (IOException e) {
			BookStoreApp.displayDialog(AlertType.ERROR, FAILED_WRITE_TITLE, FAILED_WRITE_HEADER, FAILED_WRITE_CONTENT);
			e.printStackTrace();
		}
	}
}
