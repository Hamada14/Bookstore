package view;

import java.util.ArrayList;
import java.util.List;

import client.BookClient;
import client.alphabit.BookStoreApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import server.ResponseData;
import server.database.entities.publisher.Publisher;
import server.database.entities.publisher.PublisherAddress;
import server.database.entities.publisher.PublisherPhone;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class PublisherManagementController implements CustomController {

	private static final String PUBLISHER_ADDED_SUCCESSFULLY = "Publisher add successfully";
	private static final String SUCCESS = "Success!";
	private static final String PHONE_ADDED_SUCCESSFULLY = "Phone added successfully";
	private static final String PHONE_DELETED_SUCCESSFULLY = "Phone deleted successfully";
	private static final String ADDRESS_ADDED_SUCCESSFULLY = "Address added successfully";
	private static final String NOTHING_SELECTED = "Nothing selected";
	private static final String INVALID_SELECTION = "Invalid selection";
	private static final String ADDRESS_DELETED_SUCCESSFULLY = "Address deleted successfully";

	private static final String ERROR = "ERROR!";

	private static final String COMA = ",";

	@FXML
	private TextField publisherName;
	@FXML
	private TextField phoneNumber;
	@FXML
	private TextField street;
	@FXML
	private TextField city;
	@FXML
	private TextField country;
	@FXML
	private Label fullName;

	@FXML
	private ComboBox<String> addressesCombo;
	@FXML
	private ComboBox<String> phonesCombo;

	private ObservableList<String> addressesList;
	private ObservableList<String> phonesList;

	private String usedPublisher;

	@FXML
	private void loadInformation() {
		usedPublisher = publisherName.getText();
		List<PublisherAddress> pAddresses = BookClient.getServer()
				.loadPublisherAddresses(BookStoreApp.getUser().getIdentity(), new Publisher(usedPublisher));
		List<PublisherPhone> pPhones = BookClient.getServer().loadPublisherPhones(BookStoreApp.getUser().getIdentity(),
				new Publisher(usedPublisher));
		addressesList = getAddresses(pAddresses);
		phonesList = getPhones(pPhones);
		addressesCombo.setItems(addressesList);
		phonesCombo.setItems(phonesList);
	}

	private ObservableList<String> getAddresses(List<PublisherAddress> lst) {
		List<String> adds = new ArrayList<>();
		for (PublisherAddress pd : lst) {
			adds.add(composeAddress(pd.getStreet(), pd.getCity(), pd.getCountry()));
		}
		return FXCollections.observableArrayList(adds);
	}

	private ObservableList<String> getPhones(List<PublisherPhone> lst) {
		List<String> phNums = new ArrayList<>();
		for (PublisherPhone pp : lst) {
			phNums.add(pp.getNumber());
		}
		return FXCollections.observableArrayList(phNums);
	}

	@FXML
	private void addPublisher() {
		usedPublisher = publisherName.getText();
		ResponseData rs = BookClient.getServer().addPublisher(BookStoreApp.getUser().getIdentity(),
				new Publisher(usedPublisher));
		response(PUBLISHER_ADDED_SUCCESSFULLY, rs);
	}

	@FXML
	private void addPhone() {
		String pNum = phoneNumber.getText();
		ResponseData rs = BookClient.getServer().addPublisherPhone(BookStoreApp.getUser().getIdentity(),
				new Publisher(usedPublisher), new PublisherPhone(pNum));
		response(PHONE_ADDED_SUCCESSFULLY, rs);
		if (rs.isSuccessful()) {
			phonesList.add(pNum);
			phonesCombo.setItems(phonesList);
		}
	}

	@FXML
	private void deletePhone() {
		String removeP = phonesCombo.getValue();
		ResponseData rs = BookClient.getServer().deletePublisherPhone(BookStoreApp.getUser().getIdentity(),
				new Publisher(usedPublisher), new PublisherPhone(removeP));
		response(PHONE_DELETED_SUCCESSFULLY, rs);
		if (rs.isSuccessful()) {
			phonesCombo.getItems().remove(removeP);
			phonesList.remove(removeP);
		}
	}

	@FXML
	private void addAddress() {
		PublisherAddress pAddress = new PublisherAddress(street.getText(), city.getText(), country.getText());
		ResponseData rs = BookClient.getServer().addPublisherAddress(BookStoreApp.getUser().getIdentity(),
				new Publisher(usedPublisher), pAddress);
		response(ADDRESS_ADDED_SUCCESSFULLY, rs);
		if (rs.isSuccessful()) {
			addressesList.add(composeAddress(pAddress.getCity(), pAddress.getCountry(), pAddress.getCountry()));
			addressesCombo.setItems(addressesList);
		}
	}

	@FXML
	private void deleteAddress() {
		String add = addressesCombo.getValue();
		if (add == null) {
			BookStoreApp.displayDialog(AlertType.ERROR, ERROR, null, NOTHING_SELECTED);
			return;
		}
		String[] fields = add.split(COMA);
		if (fields.length != 3) {
			BookStoreApp.displayDialog(AlertType.ERROR, ERROR, null, INVALID_SELECTION);
			return;
		}
		ResponseData rs = BookClient.getServer().deletePublisherAddress(BookStoreApp.getUser().getIdentity(),
				new Publisher(usedPublisher), new PublisherAddress(fields[0], fields[1], fields[2]));
		response(ADDRESS_DELETED_SUCCESSFULLY, rs);
		if(rs.isSuccessful()) {
			addressesCombo.getItems().remove(add);
			addressesList.remove(add);
		}
	}

	private String composeAddress(String s, String ct, String ctry) {
		return s + COMA + ct + COMA + ctry;
	}

	private void response(String suc, ResponseData rs) {
		if (rs.isSuccessful()) {
			BookStoreApp.displayDialog(AlertType.INFORMATION, SUCCESS, null, suc);
		} else {
			BookStoreApp.displayDialog(AlertType.ERROR, ERROR, null, rs.getError());
		}
	}

	@FXML
	private void goHome() {
		BookStoreApp.showCustomer(true);
	}
	
	@Override
	public void initData(Parameters parameters) {
		addressesList = FXCollections.observableArrayList();
		phonesList = FXCollections.observableArrayList();
		usedPublisher = "";
		fullName.setText(BookStoreApp.getUser().getFullName());
	}
}
