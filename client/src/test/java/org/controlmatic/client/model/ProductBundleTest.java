package org.controlmatic.client.model;

import org.controlmatic.client.controller.ProductBundleController;
import org.controlmatic.shared.domain.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Same principles as on the ShoppingCartTest class
 */

public class ProductBundleTest {
    private Product testProduct;
    private ProductBundleController bundle;

    @BeforeEach
    protected void setUp(){
        testProduct = new Product(1, "Banana", new BigDecimal("1.50"), 14, "123456789", Collections.emptyList(), Collections.emptyList());
        bundle = new ProductBundleController(testProduct);
    }

    @AfterEach
    protected void tearDown(){
        testProduct = null;
        bundle = null;
    }

    @Test
    void testBundle(){
        assertEquals(testProduct.hashCode(), bundle.getProduct().hashCode());
    }

    @Test
    void priceCalculations() {
        assertEquals(1, bundle.getAmount());
        assertEquals(new BigDecimal("1.71"), bundle.getTotalPrice(false).stripTrailingZeros());

        bundle.setAmount(3);
        assertEquals(new BigDecimal("5.13"), bundle.getTotalPrice(false).stripTrailingZeros());

        bundle.setDiscountPercentage(30);
        assertEquals(new BigDecimal("3.59"), bundle.getTotalPrice(false).stripTrailingZeros());

        bundle.setDiscountPercentage(100);
        assertEquals(new BigDecimal(0), bundle.getTotalPrice(false).stripTrailingZeros());
    }

    @Test
    void testSetAmount(){
        bundle.setAmount(3);
        assertEquals(3, bundle.getAmount());
    }

    @Test
    void testAddProducts(){
        bundle.setAmount(3);
        assertEquals(new BigDecimal("5.13"), bundle.getTotalPrice(false ).stripTrailingZeros());
    }
}
