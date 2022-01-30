package org.controlmatic.client.controller;

import org.controlmatic.client.model.ProductBundle;
import org.controlmatic.client.model.ProductBundlePane;
import org.controlmatic.shared.domain.Product;
import java.math.BigDecimal;

/**
 * Controller for the product bundle.
 */
public class ProductBundleController {
    ProductBundle bundle;

    ProductBundlePane cashierPBP;
    ProductBundlePane customerPBP;


    public ProductBundleController(Product product){
        bundle = new ProductBundle(product);

    }

    public Product getProduct() {
        return bundle.getProduct();
    }




    public void initPanes(CashierViewController controller, Boolean isBonusCustomer){
        cashierPBP = createProductBundlePane(controller, isBonusCustomer);
        customerPBP = createProductBundlePane(controller, isBonusCustomer);
    }

    private ProductBundlePane createProductBundlePane(CashierViewController controller, Boolean isBonusCustomer){
        ProductBundlePane productBundlePane = new ProductBundlePane(this, controller, isBonusCustomer);
        return productBundlePane;
    }




    public ProductBundlePane getCashierPBP() {
        return cashierPBP;
    }


    public ProductBundlePane getCustomerPBP() {
        return customerPBP;
    }


    public void updatePanes(Boolean isBonusCustomer){
        cashierPBP.update(isBonusCustomer);
        customerPBP.update(isBonusCustomer);
    }



    // ================ Actions =====================


    public void unselect(){
        cashierPBP.unselect();
    }


    public void select(){
        cashierPBP.select();
    }



    // ================== Price ====================


    /**
     * Gets the total price for the products in the bundle, including VAT and taking discount in mind.
     *
     * @return the total price
     */
    public BigDecimal getTotalPrice(Boolean isBonusCustomer) {

        return bundle.getTotalPrice(isBonusCustomer);
    }

    public void setDiscountPercentage(int discountPercentage) {
        bundle.setDiscountPercentage(discountPercentage);
    }

    public int getDiscountPercentage() {
        return bundle.getDiscountPercentage();
    }

    // ================== Amount ===================

    /**
     * Sets the amount of the PB. Min value is 1.
     * @param amount
     */
    public void setAmount(int amount) {
        bundle.setAmount(amount);
    }

    public int getAmount() {
        return bundle.getAmount();
    }


    public void addToAmount(int value){
        setAmount(getAmount() + value);
    }
}
