package org.controlmatic.client.service;

import org.controlmatic.shared.domain.*;
import org.controlmatic.shared.request.ProductUpdateRequest;
import org.controlmatic.shared.request.SalesCreateRequest;
import org.controlmatic.shared.request.SalesQueryRequest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * End-to-end server tests.
 */
public class ServerServiceTest {
    private static final ServerService serverService = new ServerService("http://localhost:8080");

    // Enable to test locally, mocking would probably be above the scope of this course :)
    // This requires a special setup.
    //@Test
    void all() throws WebClientException {
        // Customers
        List<Customer> customers = serverService.getAllCustomers();
        assertEquals(0, customers.size());

        Customer customer = serverService.getCustomerById(1);
        assertEquals(1, customer.getId());

        Customer customer2 = serverService.getCustomerByCard("133777", 9, 22);
        assertEquals(1, customer2.getId());

        // Products
        List<Product> products = serverService.getAllProducts();
        assertTrue(products.size() > 0);

        Product product = serverService.getProductById(1);
        assertEquals(1, product.getId());

        Product product2 = serverService.getProductByBarcode(product.getBarcode());
        assertEquals(1, product2.getId());

        ProductUpdateRequest updateRequest = new ProductUpdateRequest();
        updateRequest.setPrice(new BigDecimal("13.37"));
        Product updatedProduct = serverService.updateProduct(1, updateRequest);
        assertEquals(updateRequest.getPrice(), updatedProduct.getPrice());

        List<Product> searchedProduct = serverService.searchProducts(product.getName().toUpperCase(Locale.ROOT));
        assertEquals(1, searchedProduct.stream().filter(p -> p.getId() == 1).count());

        // Sales
        List<Sale> sales = serverService.getAllSales();
        assertEquals(0, sales.size());

        SalesCreateRequest createRequest = new SalesCreateRequest();
        createRequest.setCustomerId(customer.getId());
        createRequest.addProduct(new SaleProduct(product.getId(), 420));
        createRequest.setTotalPrice(updatedProduct.getPrice().multiply(new BigDecimal("420")));
        createRequest.setTimestamp(LocalDateTime.now());
        serverService.createSale(createRequest);

        List<Sale> productSales = serverService.getSalesByProductId(product.getId());
        assertEquals(1, productSales.size());

        SalesQueryRequest queryRequest = new SalesQueryRequest();
        queryRequest.setDateTimeStart(LocalDateTime.now().minusDays(1));
        queryRequest.setDateTimeEnd(LocalDateTime.now());
        List<Sale> queriedSales = serverService.querySales(queryRequest);
        assertEquals(1, queriedSales.size());

        // Customers
        //List<Customer> searchedCustomers = serverService.searchCustomers(customer.getFirstName());
        //assertTrue(searchedCustomers.size() > 0);
    }
}
