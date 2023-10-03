package application.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.util.StringConverter;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import smsHandy.PrepaidSmsHandy;
import smsHandy.Provider;
import smsHandy.SmsHandy;
import smsHandy.TariffPlanSmsHandy;

import java.io.IOException;

import application.MainApp;

public class SmsHandyOverviewController {
	@FXML
	private TableView<SmsHandy> smsHandyTable;
	@FXML
	private TableColumn<SmsHandy, String> phoneNumberColumn;
	@FXML
	private TableColumn<SmsHandy, String> providerColumn;
	@FXML
	private TableColumn<SmsHandy, String> typeColumn;

	@FXML
	private Label amountDetailsLabel;

	@FXML
	private ComboBox<Provider> providersComboBox;

	// Reference to the main application.
	private MainApp mainApp;

	/**
	 * The constructor. The constructor is called before the initialize() method.
	 */
	public SmsHandyOverviewController() {
	}

	/**
	 * Initializes the controller class. This method is automatically called after
	 * the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {

		// Initialize the smsHandy table.
		phoneNumberColumn.setCellValueFactory(cellData -> cellData.getValue().numberProperty());
		providerColumn.setCellValueFactory(cellData -> {
			if (cellData.getValue().getProvider() instanceof Provider) {
				return cellData.getValue().getProvider().nameProperty();
			} else {
				return new SimpleStringProperty("null");
			}

		});

		typeColumn.setCellValueFactory(cellData -> {
			if (cellData.getValue() instanceof PrepaidSmsHandy) {
				return new SimpleStringProperty("Prepaid");
			} else if (cellData.getValue() instanceof TariffPlanSmsHandy) {
				return new SimpleStringProperty("TariffPlan");
			}
			return null;
		});

		// Define rendering of the list of values in ComboBox drop down.
		providersComboBox.setCellFactory((comboBox) -> {
			return new ListCell<Provider>() {
				@Override
				protected void updateItem(Provider item, boolean empty) {
					super.updateItem(item, empty);

					if (item == null || empty) {
						setText(null);
					} else {
						setText(item.getName());
					}
				}
			};
		});

		// Define rendering of selected value shown in ComboBox.
		providersComboBox.setConverter(new StringConverter<Provider>() {
			@Override
			public String toString(Provider p) {
				if (p == null) {
					return null;
				} else {
					return p.getName();
				}
			}

			@Override
			public Provider fromString(String providerString) {
				return null; // No conversion fromString needed.
			}
		});

		// Clear SmsHandy details.
		showDisplayAmountDetails(null);

		// Listen for selection changes and show the SmsHandy amount details when
		// changed.
		smsHandyTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showDisplayAmountDetails(newValue));

	}

	/**
	 * Is called by the main application to give a reference back to itself.
	 * 
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;

		// Add observable list data to the table
		smsHandyTable.setItems(mainApp.getSmsHandyData());

		// Init ComboBox items.
		providersComboBox.setItems(mainApp.getProviderData());
	}

	/**
	 * Fills all text fields to show details about the SmsHandy. If the specified
	 * SmsHandy is null, all text fields are cleared.
	 * 
	 * @param s the smsHandy or null
	 */
	private void showDisplayAmountDetails(SmsHandy s) {
		if (s != null) {
			if (s instanceof PrepaidSmsHandy) {

				amountDetailsLabel.setText(
						" Remaining Credit : " + String.valueOf(s.getProvider().getCreditForSmsHandy(s.getNumber())));
			} else if (s instanceof TariffPlanSmsHandy) {

				amountDetailsLabel
						.setText(" Free SMS : " + String.valueOf(((TariffPlanSmsHandy) s).getRemainingFreeSms()));
			}

		} else {
			amountDetailsLabel.setText("");
		}
	}

	/**
	 * Called when the user clicks on the delete button.
	 */
	@FXML
	private void handleDeleteSmsHandy() {
		int selectedIndex = smsHandyTable.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			smsHandyTable.getItems().remove(selectedIndex);
		} else {
			// Nothing selected.
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("No Selection");
			alert.setHeaderText("No SmsHandy Selected");
			alert.setContentText("Please select a SmsHandy in the table.");

			alert.showAndWait();
		}
	}

	@FXML
	private void handleChangeAssignment() {

		int selectedIndex = smsHandyTable.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			SmsHandy selectedSmsHandy = smsHandyTable.getItems().get(selectedIndex);
			Provider selectedProvider = providersComboBox.getSelectionModel().getSelectedItem();
			if (selectedProvider != null) {
				selectedSmsHandy.setProvider(selectedProvider);
				mainApp.getSmsHandyData().set(selectedIndex, selectedSmsHandy);
			} else {
				// Nothing selected.
				Alert alert = new Alert(AlertType.WARNING);
				alert.initOwner(mainApp.getPrimaryStage());
				alert.setTitle("No Selection");
				alert.setHeaderText("No Provider Selected");
				alert.setContentText("Please select a provider in the comboBox.");

				alert.showAndWait();
			}
		} else {
			// Nothing selected.
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("No Selection");
			alert.setHeaderText("No SmsHandy Selected");
			alert.setContentText("Please select a SmsHandy in the table.");

			alert.showAndWait();
		}

	}

	/**
	 * Called when the user clicks the new button. Opens a dialog to add new
	 * SmsHandy.
	 */
	@FXML
	private void handleNewSmsHandy() {
		try {
			mainApp.showNewSmsHandyDialog();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Called when the user clicks the button. Opens a dialog to manage providers
	 */
	@FXML
	private void handleManangeProviders() {
		mainApp.showManageProvidersDialog();
	}

	/**
	 * Called when the user clicks the button. Opens a dialog to Send Message
	 */
	@FXML
	private void handleSendMessage() {
		int selectedIndex = smsHandyTable.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			SmsHandy selectedSmsHandy = smsHandyTable.getItems().get(selectedIndex);
			mainApp.showSendMessageDialog(selectedSmsHandy);
		} else {
			// Nothing selected.
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("No Selection");
			alert.setHeaderText("No SmsHandy Selected");
			alert.setContentText("Please select a SmsHandy in the table.");

			alert.showAndWait();
		}
	}

	/**
	 * Called when the user clicks the button. Opens a dialog to Sent Message
	 */
	@FXML
	private void handleSentMessage() {
		int selectedIndex = smsHandyTable.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			SmsHandy selectedSmsHandy = smsHandyTable.getItems().get(selectedIndex);
			mainApp.showSentMessageDialog(selectedSmsHandy);
		} else {
			// Nothing selected.
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("No Selection");
			alert.setHeaderText("No SmsHandy Selected");
			alert.setContentText("Please select a SmsHandy in the table.");

			alert.showAndWait();
		}

	}

	/**
	 * Called when the user clicks the button. Opens a dialog to Received Message
	 */
	@FXML
	private void handleReceivedMessage() {

		int selectedIndex = smsHandyTable.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			SmsHandy selectedSmsHandy = smsHandyTable.getItems().get(selectedIndex);
			mainApp.showReceivedMessageDialog(selectedSmsHandy);
		} else {
			// Nothing selected.
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("No Selection");
			alert.setHeaderText("No SmsHandy Selected");
			alert.setContentText("Please select a SmsHandy in the table.");

			alert.showAndWait();
		}

	}

}
