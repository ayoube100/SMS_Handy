package application.view;

import application.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import smsHandy.PrepaidSmsHandy;
import smsHandy.Provider;
import smsHandy.SmsHandy;
import smsHandy.TariffPlanSmsHandy;

public class SendMessageController {

	@FXML
	private TextField toField;
	@FXML
	private TextArea contentField;

	private Stage dialogStage;
	private SmsHandy smsHandy;

	/**
	 * The constructor (is called before the initialize()-method).
	 */
	public SendMessageController() {
	}

	/**
	 * Initializes the controller class. This method is automatically called after
	 * the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
	}

	/**
	 * Sets the smsHandy
	 * 
	 * @param smsHandy
	 */
	public void setSmsHandy(SmsHandy smsHandy) {
		this.smsHandy = smsHandy;
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
	 * Called when the user clicks Send.
	 */
	@FXML
	private void handleSendButton() {
		if (isInputValid()) {
			String msg = "";

			if (this.smsHandy.getProvider() == null) {
				msg = "This mobile phone " + this.smsHandy.getNumber() + " not affected to eny provider.";
			}else if((Provider.findProvider(toField.getText()) == null) && (!toField.getText().equals("*101#")) ) {
				msg = "this recipient mobile phone " + toField.getText() + " not affected to eny provider";
			}else {
				boolean check = this.smsHandy.sendSms(toField.getText(), contentField.getText());
				if (check) {
					msg = "Your message sent successfully.";
				} else {
					if (this.smsHandy instanceof TariffPlanSmsHandy) {
						msg = "Sorry you can't send any more sms you ran out of balance.";
					} else if (this.smsHandy instanceof PrepaidSmsHandy) {
						msg = "Sorry you can't send SMS your amount of free SMS is spent.";
					}
				}
			}

			// Show the message.
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.initOwner(dialogStage);
			alert.setTitle("Message Info");
			alert.setContentText(msg);
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

		if (toField.getText() == null || toField.getText().length() == 0) {
			errorMessage += "No valid to field!\n";
		}

		if (contentField.getText() == null || contentField.getText().length() == 0) {
			errorMessage += "No valid content field!\n";
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
