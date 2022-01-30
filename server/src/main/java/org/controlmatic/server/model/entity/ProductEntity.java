package org.controlmatic.server.model.entity;

import org.controlmatic.shared.domain.ProductBuilder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class for additional product information.
 */
@Entity
public class ProductEntity {
    @Id
    private Integer id;

    private BigDecimal price;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable
    private List<ProductOfferEntity> offers;

    protected ProductEntity() {}

    public ProductEntity(Integer id, BigDecimal price, List<ProductOfferEntity> offers) {
        this.id = id;
        this.price = price;
        this.offers = offers;
    }

    public Integer getId() {
        return id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public List<ProductOfferEntity> getOffers() {
        return offers;
    }

    public void setOffers(List<ProductOfferEntity> offers) {
        this.offers = offers;
    }

    /**
     * Sets fields in a {@link ProductBuilder} with data from this object.
     *
     * @param builder the builder to set fields in
     */
    public void decorate(ProductBuilder builder) {
        builder.id(id).price(price).offers(offers != null ? offers.stream().map(ProductOfferEntity::toOffer).collect(Collectors.toList()) : Collections.emptyList());
    }
}
