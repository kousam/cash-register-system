package org.controlmatic.server.model.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Class for sales.
 */
@Entity
public class SaleEntity {
    @Id
    @GeneratedValue
    private Integer id;

    private Integer customerId;

    @ElementCollection
    @CollectionTable
    private List<SaleProductEntity> products;

    private BigDecimal totalPrice;
    private LocalDateTime timestamp;

    protected SaleEntity() {}

    public SaleEntity(Integer customerId, List<SaleProductEntity> products, BigDecimal totalPrice, LocalDateTime timestamp) {
        this.customerId = customerId;
        this.products = products;
        this.totalPrice = totalPrice;
        this.timestamp = timestamp;
    }

    public Integer getId() {
        return id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public List<SaleProductEntity> getProducts() {
        return products;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
