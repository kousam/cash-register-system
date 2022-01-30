package org.controlmatic.server.model.entity;

import org.controlmatic.shared.domain.SaleProduct;

import javax.persistence.Embeddable;

/**
 * Class for representing a product within a sale.
 */
@Embeddable
public class SaleProductEntity {
    private Integer id;
    private Integer amount;

    protected SaleProductEntity() {}

    public SaleProductEntity(int id, int amount) {
        this.id = id;
        this.amount = amount;
    }

    public SaleProductEntity(SaleProduct saleProduct) {
        this(saleProduct.getId(), saleProduct.getAmount());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
