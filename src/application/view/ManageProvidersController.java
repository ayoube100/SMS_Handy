package application.view;


import application.MainApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import smsHandy.Provider;
import smsHandy.SmsHandy;

public class ManageProvidersController {

	@FXML
	private TextField providerNameField;

	@FXML
	private ListView<Provider> providersListView;

	private Stage dialogStage;
	private MainApp mainApp;

	/**
	 * The constructor (is called before the initialize()-method).
	 */
	public ManageProvidersController() {}

	/**
	 * Initializes the controller class. This method is automatically called after
	 * the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		
		providersListView.setCellFactory((list) -> {
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
		

	}
	
	/**
	 * Is called by the main application to give a reference back to itself.
	 * 
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		// Init ListView.
		providersListView.setItems(mainApp.getProviderData());
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
	 * Called when the user clicks cancel.
	 */
	@FXML
	private void handleCancel() {
		dialogStage.close();
	}
	
	
	/**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {
            mainApp.getProviderData().add(new Provider(providerNameField.getText()));
            providerNameField.setText("");
        }
    }
	
	/**
	 * Called when the user clicks on the delete button.
	 */
	@FXML
	private void handleDeleteProvider() {
		int selectedIndex = providersListView.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			
			Provider selectedProvider = providersListView.getItems().get(selectedIndex);
			
			for(int i = 0; i < Provider.providers.size(); i++) {
				if(Provider.providers.get(i).getName().equals(selectedProvider.getName()))
					Provider.providers.remove(i);			
			}
			
			for(int i = 0; i < mainApp.getSmsHandyData().size(); i++) {
				if(mainApp.getSmsHandyData().get(i).getProvider() == selectedProvider) {
					SmsHandy s = mainApp.getSmsHandyData().get(i);
					s.setProvider(null);
					mainApp.getSmsHandyData().set(i, s);
				}
			}
			
			providersListView.getItems().remove(selectedIndex);	
	
			
		} else {
			// Nothing selected.
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(dialogStage);
			alert.setTitle("No Selection");
			alert.setHeaderText("No Provider Selected");
			alert.setContentText("Please select a Provider in the List.");

			alert.showAndWait();
		}
	}

	/**
	 * Validates the user input in the text fields.
	 * 
	 * @return true if the input is valid
	 */
	private boolean isInputValid() {
		String errorMessage = "";

		if (providerNameField.getText() == null || providerNameField.getText().length() == 0) {
			errorMessage += "No valid name!\n";
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
