package org.controlmatic.client.model;

import org.controlmatic.client.controller.ProductBundleController;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Class for the properties of shopping cart.
 */
 
public class ShoppingCart {
    ArrayList<ProductBundleController> productBundles;
    int bonusCustomerId = 0;

    public ShoppingCart(){

        productBundles = new ArrayList<ProductBundleController>();
    }

    public BigDecimal getTotalPrice(Boolean isBonusCustomer){
        BigDecimal sum = new BigDecimal(0);
        for(ProductBundleController i: getProductBundles()){
            sum = sum.add(i.getTotalPrice(isBonusCustomer));
        }
        return sum;
    }

    public int getBonusCustomerId() {
        return bonusCustomerId;
    }

    public void setBonusCustomerId(int id) {
        bonusCustomerId = id;
        System.out.println("Bonus Customer Scanned!");
    }

    public void addBundle(ProductBundleController bundle){
        productBundles.add(bundle);
    }

    public void removeBundle(ProductBundleController bundle){
        productBundles.remove(bundle);
    }

    public void clear(){
        productBundles.clear();
    }

    public ArrayList<ProductBundleController> getProductBundles(){
        return productBundles;
    }






}
