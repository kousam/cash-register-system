package org.controlmatic.client.model;

import org.controlmatic.shared.domain.SaleProduct;

/**
 * A product bundle in the sales context
 */
public class SaleProductBundle {

    String productName;
    int amount;

    /**
     * Creates a new {@link SaleProductBundle}
     * kind of a hack to combine results for salesdataview
     *
     * @param name the product name
     *
     */
    public SaleProductBundle(String name){
        productName = name;
        amount = 0;
    }

    public void addSaleProduct(SaleProduct saleProduct){
        if (saleProduct.getName().equals(productName)) {
            amount += saleProduct.getAmount();
        }
    }

    public String getProductName(){
        return productName;
    }

    public int getAmount(){
        return amount;
    }
}
