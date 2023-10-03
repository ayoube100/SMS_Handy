package application;

import java.io.IOException;

import application.view.ManageProvidersController;
import application.view.ReceivedMessageController;
import application.view.SendMessageController;
import application.view.SentMessageController;
import application.view.SmsHandyAddController;
import application.view.SmsHandyOverviewController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import smsHandy.PrepaidSmsHandy;
import smsHandy.Provider;
import smsHandy.SmsHandy;
import smsHandy.TariffPlanSmsHandy;

public class MainApp extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;

	/**
	 * The data as an observable list of smsHand.
	 */
	private ObservableList<SmsHandy> smsHandyData = FXCollections.observableArrayList();
	/**
	 * The data as an observable list of providers.
	 */
	private ObservableList<Provider> providerData = FXCollections.observableArrayList();

	/**
	 * Constructor
	 */
	public MainApp() {

		Provider inwi = new Provider("inwi");
		Provider orange = new Provider("orange");
		Provider marocTelecom = new Provider("marocTelecom");
		providerData.add(inwi);
		providerData.add(orange);
		providerData.add(marocTelecom);

		// Add some sample data
		smsHandyData.add(new PrepaidSmsHandy("+212 638 28 06 10", inwi));
		smsHandyData.add(new TariffPlanSmsHandy("+212 674 68 36 10", orange));
		smsHandyData.add(new PrepaidSmsHandy("+212 648 58 76 20", orange));
		smsHandyData.add(new PrepaidSmsHandy("+212 618 48 86 90", marocTelecom));
		smsHandyData.add(new TariffPlanSmsHandy("+212 628 48 36 70", marocTelecom));
		smsHandyData.add(new PrepaidSmsHandy("+212 698 28 36 50", inwi));
		smsHandyData.add(new TariffPlanSmsHandy("+212 638 28 36 40", inwi));
		smsHandyData.add(new TariffPlanSmsHandy("+212 654 38 56 56", marocTelecom));
		smsHandyData.add(new PrepaidSmsHandy("+212 612 23 36 13", orange));
	}

	/**
	 * Returns the data as an observable list of SmsHandy.
	 * 
	 * @return
	 */
	public ObservableList<SmsHandy> getSmsHandyData() {
		return smsHandyData;
	}

	/**
	 * Returns the data as an observable list of Providers.
	 * 
	 * @return
	 */
	public ObservableList<Provider> getProviderData() {
		return providerData;
	}

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("SmsHandy_HamzaBoubouh");

		initRootLayout();

		showSmsHandyOverview();
	}

	/**
	 * Initializes the root layout.
	 */
	public void initRootLayout() {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

			// Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Shows the smsHandy overview inside the root layout.
	 */
	public void showSmsHandyOverview() {
		try {
			// Load smsHandy overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/SmsHandyOverview.fxml"));
			AnchorPane smsHandyOverview = (AnchorPane) loader.load();

			// Set smsHandy overview into the center of root layout.
			rootLayout.setCenter(smsHandyOverview);

			// Give the controller access to the main app.
			SmsHandyOverviewController controller = loader.getController();
			controller.setMainApp(this);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Shows the provider dialog.
	 */
	public void showManageProvidersDialog() {

		// Load the fxml file and create a new stage for the popup dialog.
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApp.class.getResource("view/ManageProvidersDialog.fxml"));
		AnchorPane page = null;
		try {
			page = (AnchorPane) loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Create the dialog Stage.
		Stage dialogStage = new Stage();
		dialogStage.setTitle("Manage Providers");
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.initOwner(primaryStage);
		Scene scene = new Scene(page);
		dialogStage.setScene(scene);

		ManageProvidersController controller = loader.getController();
		controller.setDialogStage(dialogStage);
		controller.setMainApp(this);

		// Show the dialog and wait until the user closes it
		dialogStage.showAndWait();

	}

	/**
	 * Shows the SmsHandy Add dialog.
	 * 
	 * @throws IOException
	 */
	public void showNewSmsHandyDialog() throws IOException {

		// Load the fxml file and create a new stage for the popup dialog.
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApp.class.getResource("view/SmsHandyAdd.fxml"));

		AnchorPane page = (AnchorPane) loader.load();

		// Create the dialog Stage.
		Stage dialogStage = new Stage();
		dialogStage.setTitle("Add SmsHandy");
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.initOwner(primaryStage);
		Scene scene = new Scene(page);
		dialogStage.setScene(scene);

		SmsHandyAddController controller = loader.getController();
		controller.setDialogStage(dialogStage);
		controller.setMainApp(this);

		// Show the dialog and wait until the user closes it
		dialogStage.showAndWait();
	}

	public void showSendMessageDialog(SmsHandy s) {

		// Load the fxml file and create a new stage for the popup dialog.
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApp.class.getResource("view/SendMessageDialog.fxml"));

		AnchorPane page = null;
		try {
			page = (AnchorPane) loader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Create the dialog Stage.
		Stage dialogStage = new Stage();
		dialogStage.setTitle("Send Message");
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.initOwner(primaryStage);
		Scene scene = new Scene(page);
		dialogStage.setScene(scene);

		SendMessageController controller = loader.getController();
		controller.setDialogStage(dialogStage);
		controller.setSmsHandy(s);

		// Show the dialog and wait until the user closes it
		dialogStage.showAndWait();

	}

	public void showSentMessageDialog(SmsHandy s) {

		// Load the fxml file and create a new stage for the popup dialog.
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApp.class.getResource("view/SentMessageDialog.fxml"));

		AnchorPane page = null;
		try {
			page = (AnchorPane) loader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Create the dialog Stage.
		Stage dialogStage = new Stage();
		dialogStage.setTitle("Sent Message");
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.initOwner(primaryStage);
		Scene scene = new Scene(page);
		dialogStage.setScene(scene);

		SentMessageController controller = loader.getController();
		controller.setDialogStage(dialogStage);
		controller.setSmsHandy(s);

		// Show the dialog and wait until the user closes it
		dialogStage.showAndWait();
	}

	public void showReceivedMessageDialog(SmsHandy s) {

		// Load the fxml file and create a new stage for the popup dialog.
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApp.class.getResource("view/ReceivedMessageDialog.fxml"));

		AnchorPane page = null;
		try {
			page = (AnchorPane) loader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Create the dialog Stage.
		Stage dialogStage = new Stage();
		dialogStage.setTitle("Received Message");
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.initOwner(primaryStage);
		Scene scene = new Scene(page);
		dialogStage.setScene(scene);

		ReceivedMessageController controller = loader.getController();
		controller.setDialogStage(dialogStage);
		controller.setSmsHandy(s);

		// Show the dialog and wait until the user closes it
		dialogStage.showAndWait();
	}

	/**
	 * Returns the main stage.
	 * 
	 * @return
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
