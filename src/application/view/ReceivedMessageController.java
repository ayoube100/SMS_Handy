package application.view;

import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import smsHandy.Message;
import smsHandy.SmsHandy;

public class ReceivedMessageController {
	@FXML
	private TableView<Message> receivedTable;
	@FXML
	private TableColumn<Message, String> fromColumn;
	@FXML
	private TableColumn<Message, String> contentColumn;
	@FXML
	private TableColumn<Message, Date> dateColumn;

	private Stage dialogStage;
	private SmsHandy smsHandy;
	private ObservableList<Message> receivedMessageData = FXCollections.observableArrayList();

	/**
	 * The constructor (is called before the initialize()-method).
	 */
	public ReceivedMessageController() {
	}

	/**
	 * Initializes the controller class. This method is automatically called after
	 * the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {

		// Initialize the sentTable table.
		fromColumn.setCellValueFactory(cellData -> cellData.getValue().fromProperty());
		contentColumn.setCellValueFactory(cellData -> cellData.getValue().contentProperty());
		dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());

	}

	/**
	 * Sets the smsHandy
	 * 
	 * @param smsHandy
	 */
	public void setSmsHandy(SmsHandy smsHandy) {
		this.smsHandy = smsHandy;

		for (Message m : smsHandy.getReceived()) {
			receivedMessageData.add(m);
		}
		// Add observable list data to the table
		receivedTable.setItems(receivedMessageData);
	}

	/**
	 * Sets the stage of this dialog.
	 * 
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
}
