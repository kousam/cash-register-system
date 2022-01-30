package org.controlmatic.shared.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Class for representing a sale.
 */
public class Sale {
    private final int id;
    private final int customerId;
    private final List<SaleProduct> products;
    private final BigDecimal totalPrice;
    private final LocalDateTime timestamp;

    /**
     * Creates a new {@link Sale}.
     *
     * @param id the id of the sale
     * @param customerId the customer id that bought the items
     * @param products the products in the sale
     * @param totalPrice the total price of the sale
     * @param timestamp the time of sale
     */
    public Sale(int id, int customerId, List<SaleProduct> products, BigDecimal totalPrice, LocalDateTime timestamp) {
        this.id = id;
        this.customerId = customerId;
        this.products = products;
        this.totalPrice = totalPrice;
        this.timestamp = timestamp;
    }

    /**
     * Empty constructor for serialization.
     */
    public Sale() {
        this.id = 0;
        this.customerId = 0;
        this.products = Collections.emptyList();
        this.totalPrice = null;
        this.timestamp = null;
    }

    public int getId() {
        return id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public List<SaleProduct> getProducts() {
        return products;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
