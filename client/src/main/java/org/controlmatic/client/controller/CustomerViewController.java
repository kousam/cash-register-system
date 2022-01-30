package org.controlmatic.client.controller;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Controller for Customer View, including current price and bonus status
 */
public class CustomerViewController {


    @FXML
    VBox shoppingCart;

    @FXML
    Label totalSum;

    @FXML
    Label totalPaidAmount;

    @FXML
    Label changeLabel;

    private CashierViewController cashierViewController;


    public void setCashierViewController(CashierViewController cashierViewController){
        this.cashierViewController = cashierViewController;
    }


    public void updateCart(Pane pane){
        shoppingCart.getChildren().add(0, pane);
    }

    public void updateTotalSum(String updatedPriceString){
        totalSum.setText(updatedPriceString);
    }
    public void updateChangeSum(String updatedChangeSum){
        changeLabel.setText(updatedChangeSum);
    }
    public void updateTotalPaidAmount(String updatedPaidAmountString){ totalPaidAmount.setText(updatedPaidAmountString);}

    public void removeItem(Pane pane){
        shoppingCart.getChildren().remove(pane);
    }

    public void clearCart(){
        shoppingCart.getChildren().clear();
    }
}
