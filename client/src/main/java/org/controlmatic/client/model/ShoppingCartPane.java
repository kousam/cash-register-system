package org.controlmatic.client.model;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.controlmatic.client.controller.CashierViewController;
import org.controlmatic.client.controller.ShoppingCartController;

import java.io.IOException;

/**
 * Class for shopping cart pane.
 */

public class ShoppingCartPane extends Pane{

    CashierViewController controller;

    Label dateTimeLabel;


    Pane pane;

    public ShoppingCartPane(ShoppingCartController shoppingCart, CashierViewController controller){
        this.controller = controller;

        try {
            pane = FXMLLoader.load(getClass().getClassLoader().getResource("view/ShoppingCartShelfView.fxml"));
            pane.getStylesheets().add(getClass().getClassLoader().getResource("stylesheet.css").toString());

            dateTimeLabel = (Label) pane.lookup("#dateTimeLabel");


            pane.setOnMouseClicked(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent e) {
                    controller.setShoppingCartFromShelf(shoppingCart);

                }
            });


            getChildren().add(pane);

            update();

        } catch (IOException e){
            System.out.println("could not find resources");
        }





    }

    public void update(){

    }

    public void setDateTime(String dateTime){
        dateTimeLabel.setText(dateTime);
    }


}
