package org.controlmatic.client.controller;

import org.controlmatic.client.model.ProductBundlePane;
import org.controlmatic.client.model.ShoppingCart;
import org.controlmatic.client.model.ShoppingCartPane;
import org.controlmatic.client.util.DateTimeUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Controller for the shopping cart pane in the cashier view
 */
public class ShoppingCartController {

    ShoppingCart shoppingCart;

    private BigDecimal totalPayment = new BigDecimal(0);
    private boolean paymentStarted = false;

    ShoppingCartPane shoppingCartPane;

    String dateTime;

    public boolean isBonusCustomer(){
        return shoppingCart.getBonusCustomerId() != 0;
    }

    public int getBonusCustomerId() {
        return shoppingCart.getBonusCustomerId();
    }

    public void setBonusCustomerId(int id) {
        shoppingCart.setBonusCustomerId(id);
    }

    public ShoppingCartController(){
        shoppingCart = new ShoppingCart();
        dateTime = LocalDateTime.now().format(DateTimeUtil.DEFAULT_DATE_TIME_FORMATTER);
    }

    public boolean checkComplete() {
        return totalPayment.compareTo(getTotalPrice()) >= 0 && !shoppingCart.getProductBundles().isEmpty();
    }

    public void setPaymentStarted(){
        paymentStarted = true;
    }

    public void addPayment(BigDecimal paymentAmount){
        totalPayment = totalPayment.add(paymentAmount);
    }

    public BigDecimal getTotalPaid(){
        return totalPayment;
    }

    public BigDecimal getCashOwed(){
        return (getTotalPrice().subtract(getTotalPaid()));
    }

    public boolean getPaymentStarted(){
		return paymentStarted;
	}
    
	// ================= Price =====================

    public BigDecimal getTotalPrice(){

        return shoppingCart.getTotalPrice(isBonusCustomer());
    }

    //================== Bundle stuff ========================

    public void addBundle(ProductBundleController bundle){
        shoppingCart.addBundle(bundle);
    }

    public void removeBundle(ProductBundleController bundle){
        shoppingCart.removeBundle(bundle);
    }

    public void clearCart(){
        shoppingCart.clear();
    }

    public ArrayList<ProductBundleController> getProductBundles(){
        return shoppingCart.getProductBundles();
    }

    // ==================== Pane Stuff ======================

    public void initPanes(CashierViewController controller) {
        shoppingCartPane = createShelfPane(controller);
    }

    private ShoppingCartPane createShelfPane(CashierViewController controller){
        ShoppingCartPane shoppingCartPane = new ShoppingCartPane(this, controller);
        shoppingCartPane.setDateTime(dateTime);
        return shoppingCartPane;
    }

    public ShoppingCartPane getShelfPane(){
        return shoppingCartPane;
    }


    public ArrayList<ProductBundlePane> getCashierPBPs(){
        ArrayList<ProductBundlePane> panes = new ArrayList<>();
        for(ProductBundleController bundle: shoppingCart.getProductBundles()){
            ProductBundlePane pane;

            pane = bundle.getCashierPBP();
            panes.add(pane);
        }

        return panes;
    }

    public ArrayList<ProductBundlePane> getCustomerPBPs(){
        ArrayList<ProductBundlePane> panes = new ArrayList<>();
        for(ProductBundleController bundle: shoppingCart.getProductBundles()){
            ProductBundlePane pane;

            pane = bundle.getCustomerPBP();
            panes.add(pane);
        }

        return panes;
    }


    public boolean isEmpty(){
        return getProductBundles().isEmpty();
    }

    public String getDateTime(){
        return dateTime;
    }


}
