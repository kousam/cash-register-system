package org.controlmatic.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.controlmatic.client.controller.CashierViewController;
import org.controlmatic.client.controller.CustomerViewController;

public class ClientEntrypoint extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Load application icon
        Image icon = new Image(getClass().getClassLoader().getResourceAsStream("icon.png"));

        // Creates the cashier view
        Pane pane;
        FXMLLoader cashierLoader = new FXMLLoader(getClass().getResource("/view/CashierView.fxml"));
        pane = cashierLoader.load();


        pane.getStylesheets().add(getClass().getClassLoader().getResource("stylesheet.css").toString());
        //Font.loadFont(getClass().getClassLoader().getResource("Roboto-Regular.ttf").toExternalForm(), 10);
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.getIcons().add(icon);


        // Creates the customer view
        Pane customerPane;
        Stage customerStage = new Stage();


        FXMLLoader customerLoader = new FXMLLoader(getClass().getResource("/view/CustomerView.fxml"));
        customerPane = customerLoader.load();

        customerPane.getStylesheets().add(getClass().getClassLoader().getResource("stylesheet.css").toString());
        Font.loadFont(getClass().getClassLoader().getResource("Roboto-Regular.ttf").toExternalForm(), 30);
        Scene customerScene = new Scene(customerPane);
        customerStage.setScene(customerScene);
        customerStage.getIcons().add(icon);


        CashierViewController cashierViewController = cashierLoader.getController();
        CustomerViewController customerViewController = customerLoader.getController();

        customerViewController.setCashierViewController(cashierViewController);
        cashierViewController.setCustomerViewController(customerViewController);

        cashierViewController.configureNodes();


        // Creates the admin view
        final FXMLLoader adminLoader = new FXMLLoader(getClass().getResource("/view/AdminView.fxml"));
        final Pane adminPane = adminLoader.load();
        adminPane.getStylesheets().add(getClass().getClassLoader().getResource("stylesheet.css").toString());
        final Stage adminStage = new Stage();
        adminStage.setScene(new Scene(adminPane));
        adminStage.getIcons().add(icon);

        stage.show();
        customerStage.show();
        adminStage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}
