package org.controlmatic.shared.request;

import org.controlmatic.shared.domain.SaleProduct;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Request object for creating a new sale.
 * Create a new object and use setters to add sale information.
 */
public class SalesCreateRequest {
    private int customerId;
    private List<SaleProduct> products;
    private BigDecimal totalPrice;
    private LocalDateTime timestamp;

    /**
     * Default constructor, use setters to modify the request.
     */
    public SalesCreateRequest() {
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public List<SaleProduct> getProducts() {
        return products;
    }

    public void setProducts(List<SaleProduct> products) {
        this.products = products;
    }

    public void addProduct(SaleProduct product) {
        if (products == null) {
            products = new ArrayList<>();
        }
        products.add(product);
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
