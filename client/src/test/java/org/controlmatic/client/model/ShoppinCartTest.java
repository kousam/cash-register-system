package org.controlmatic.client.model;

import org.controlmatic.client.controller.ProductBundleController;
import org.controlmatic.shared.domain.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * TestClass. The names of methods tells usually the meaning of the tests
 */

public class ShoppinCartTest {

    private ShoppingCart shoppingCart;

    //Easy way to set always a new shopping cart before testing and after to set it as null
    @BeforeEach
    protected void setUp(){
        shoppingCart = new ShoppingCart();
    }

    @AfterEach
    protected void tearDown(){
        shoppingCart = null;
    }

    @Test
    void testShoppingCart(){
        assertNotNull(shoppingCart);
    }

    @Test
    void testAddBundle(){
        Product testProduct = new Product(1, "Banana", new BigDecimal("1.50"), 14, "123456789", Collections.emptyList(), Collections.emptyList());
        ProductBundleController bundle = new ProductBundleController(testProduct);
        shoppingCart.addBundle(bundle);
        assertEquals(bundle.hashCode(), shoppingCart.getProductBundles().get(0).hashCode());
    }

    @Test
    void testTrueBonusCustomer(){
        shoppingCart.setBonusCustomerId(1337);
        assertEquals(1337, shoppingCart.getBonusCustomerId());
    }

    @Test
    void testTotalSum(){
        Product testProduct1 = new Product(1, "Banana", new BigDecimal("1.50"), 0, "123456789", Collections.emptyList(), Collections.emptyList());
        Product testProduct2 = new Product(2, "Apple", new BigDecimal("2.49"), 0, "123456789", Collections.emptyList(), Collections.emptyList());
        ProductBundleController bundle1 = new ProductBundleController(testProduct1);
        ProductBundleController bundle2 = new ProductBundleController(testProduct2);
        shoppingCart.addBundle(bundle1);
        shoppingCart.addBundle(bundle2);
        assertEquals(new BigDecimal("3.99"), shoppingCart.getTotalPrice(false).stripTrailingZeros());
    }

    @Test
    void testRemoveBundle(){
        Product testProduct1 = new Product(1, "Banana", new BigDecimal("1.50"), 0, "123456789", Collections.emptyList(), Collections.emptyList());
        Product testProduct2 = new Product(2, "Apple", new BigDecimal("2.49"), 0, "123456789", Collections.emptyList(), Collections.emptyList());
        ProductBundleController bundle1 = new ProductBundleController(testProduct1);
        ProductBundleController bundle2 = new ProductBundleController(testProduct2);
        shoppingCart.addBundle(bundle1);
        shoppingCart.addBundle(bundle2);
        shoppingCart.removeBundle(shoppingCart.getProductBundles().get(1));
        assertEquals(new BigDecimal("1.5"), shoppingCart.getTotalPrice(false).stripTrailingZeros());
    }

    @Test
    void testBundleWithSeveralProducts(){
        Product testProduct = new Product(1, "Banana", new BigDecimal("1.0"), 10, "123456789", Collections.emptyList(), Collections.emptyList());
        ProductBundleController bundle = new ProductBundleController(testProduct);
        bundle.setAmount(3);
        shoppingCart.addBundle(bundle);
        assertEquals(new BigDecimal("3.3"), shoppingCart.getTotalPrice(false).stripTrailingZeros());
    }

		
}
