package server.database.report;

import java.util.HashMap;
import java.util.Map;

public enum ReportType {

	TOP_TEN_BOOKS_REPORT("Top ten books", "jasperReports/TopTenBooks.jrxml", "TopTenBooks.pdf"), 
	TOP_FIVE_CUSTOMERS("Top five customers", "jasperReports/TopFiveUsers.jrxml", "TopFiveCustomers.pdf"), 
	SALES("Sales", "jasperReports/Sales.jrxml", "Sales.pdf");

	private static final Map<String, ReportType> TYPE_BY_NAME;
	
	private final String name;
	private final String path;
	private final String defaultPDFType;
	
	static {
		TYPE_BY_NAME = new HashMap<>();
		for(ReportType type : ReportType.values()) {
			TYPE_BY_NAME.put(type.getName(), type);
		}
	}
	
	private ReportType(final String name, final String path, final String defaultPDFType) {
		this.name = name;
		this.path = path;
		this.defaultPDFType = defaultPDFType;
	}

	public String getName() {
		return name;
	}

	public String getPath() {
		return path;
	}

	public String getdefaultPDFFile() {
		return defaultPDFType;
	}
	
	public static ReportType valueByName(final String name) {
		return TYPE_BY_NAME.get(name);
	}
}
