package application.view;

import application.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import smsHandy.PrepaidSmsHandy;
import smsHandy.Provider;
import smsHandy.TariffPlanSmsHandy;

public class SmsHandyAddController {

	@FXML
	private ComboBox<Provider> providersComboBox;
	@FXML
	private RadioButton prepaidRadio;
	@FXML
	private RadioButton tariffPlanRadio;
	@FXML
	private TextField numberField;

	private Stage dialogStage;
	private MainApp mainApp;
	private ToggleGroup radioGroup;

	public SmsHandyAddController() {
		radioGroup = new ToggleGroup();
	}

	/**
	 * Initializes the controller class. This method is automatically called after
	 * the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {

		prepaidRadio.setToggleGroup(radioGroup);
		tariffPlanRadio.setToggleGroup(radioGroup);

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
	}

	/**
	 * Called when the user clicks Add.
	 */
	@FXML
	private void handleAddSmsHandy() {

		Provider selectedProvider = providersComboBox.getSelectionModel().getSelectedItem();
		RadioButton selectedRadioButton = (RadioButton) radioGroup.getSelectedToggle();

		if (isInputValid()) {

			if ((selectedProvider != null) && (selectedRadioButton != null)) {
				switch(selectedRadioButton.getText()) {
				case "Prepaid" :
					mainApp.getSmsHandyData().add(new PrepaidSmsHandy(numberField.getText(), selectedProvider));
					break;
				case "TariffPlan" : 
					mainApp.getSmsHandyData().add(new TariffPlanSmsHandy(numberField.getText(), selectedProvider));
					break;
				}
			} else {
				// Nothing selected.
				Alert alert = new Alert(AlertType.WARNING);
				alert.initOwner(mainApp.getPrimaryStage());
				alert.setTitle("No Selection");
				alert.setContentText("Please select a provider in the comboBox and SmsHandy Type.");

				alert.showAndWait();
			}
		}

		System.out.println("Button Click");
	}

	/**
	 * Sets the stage of this dialog.
	 * 
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	/**
	 * Is called by the main application to give a reference back to itself.
	 * 
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		providersComboBox.setItems(mainApp.getProviderData());
	}

	/**
	 * Validates the user input in the text fields.
	 * 
	 * @return true if the input is valid
	 */
	private boolean isInputValid() {
		String errorMessage = "";

		if (numberField.getText() == null || numberField.getText().length() == 0) {
			errorMessage += "No valid phone Number!\n";
		}

		if (errorMessage.length() == 0) {
			return true;
		} else {
			// Show the error message.
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(dialogStage);
			alert.setTitle("Invalid Fields");
			alert.setHeaderText("Please correct invalid fields");
			alert.setContentText(errorMessage);

			alert.showAndWait();

			return false;
		}
	}

}
