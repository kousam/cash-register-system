package org.controlmatic.client.model;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.controlmatic.client.controller.CashierViewController;
import org.controlmatic.client.controller.ProductBundleController;
import org.controlmatic.client.util.StringUtil;

import java.io.IOException;

/**
 * ProductBundlePane contains a productbundle and allows it to be displayed and interacted with in the GUI.
 */

public class ProductBundlePane extends Pane {

    ProductBundleController productBundle;

    Pane pane;

    Label titleLabel;
    Label priceLabel;
    Label discountPercentLabel;
    Label priceSumLabel;
    Label amountLabel;
    Label vatLabel;

    @FXML
    Pane productBundleVBox;

    public ProductBundlePane(ProductBundleController productBundle, CashierViewController controller, Boolean isBonusCustomer){
        this.productBundle = productBundle;

        try{
            pane = FXMLLoader.load(getClass().getClassLoader().getResource("view/ProductBundleView.fxml"));
            pane.getStylesheets().add(getClass().getClassLoader().getResource("stylesheet.css").toString());


            titleLabel = (Label) pane.lookup("#title");
            priceLabel = (Label) pane.lookup("#price");
            discountPercentLabel = (Label) pane.lookup("#discountPercent");
            priceSumLabel = (Label) pane.lookup("#priceSum");
            amountLabel = (Label) pane.lookup("#amountLabel");
            vatLabel = (Label) pane.lookup("#vatLabel");

            pane.setOnMouseClicked(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent e) {
                    controller.setSelectedBundle(productBundle);

                }
            });


            getChildren().add(pane);

            update(isBonusCustomer);

        }catch (IOException e){
            System.out.println("Could not find resources");
        }

    }

    public void update(Boolean isBonusCustomer){
        String name = productBundle.getProduct().getName();
        String price = StringUtil.toUTF8(productBundle.getProduct().getPrice() + "€");
        String priceSum = productBundle.getTotalPrice(isBonusCustomer).toString();
        String amount = "";

        String percentageDiscount = "";
        String vat = "VAT " + String.valueOf(productBundle.getProduct().getVatPercentage()) + "%";

        if(productBundle.getAmount() > 1){
            amount = "x" + String.valueOf(productBundle.getAmount());
        }
        if(productBundle.getDiscountPercentage() != 0) {percentageDiscount = "-" + String.valueOf(productBundle.getDiscountPercentage()) + "%";}




        priceSum = StringUtil.toEuro(priceSum);


        titleLabel.setText(name);
        priceLabel.setText(price);
        discountPercentLabel.setText(percentageDiscount);
        priceSumLabel.setText(priceSum);
        amountLabel.setText(amount);
        vatLabel.setText(vat);

    }


    public ProductBundleController getProductBundle() {
        return productBundle;
    }

    public void updateAmount(int newAmount){
        productBundle.setAmount(newAmount);
    }

    @FXML
    public void select(){
        pane.setId("listItemSelected");
    }

    public void unselect(){

        pane.setId("listItem");
    }
}
