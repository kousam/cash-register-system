package org.controlmatic.server.model.entity;

import org.controlmatic.shared.domain.ProductOffer;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;

/**
 * Class for representing a product offer.
 */
@Embeddable
public class ProductOfferEntity {
    private Integer offerPercentage;
    private LocalDateTime start;
    private LocalDateTime end;
    private boolean onlyBonusCustomers;

    protected ProductOfferEntity() {}

    public ProductOfferEntity(ProductOffer productOffer) {
        this.offerPercentage = productOffer.getOfferPercentage();
        this.start = productOffer.getStart();
        this.end = productOffer.getEnd();
        this.onlyBonusCustomers = productOffer.isOnlyBonusCustomers();
    }

    public Integer getOfferPercentage() {
        return offerPercentage;
    }

    public void setOfferPercentage(Integer offerPercentage) {
        this.offerPercentage = offerPercentage;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public boolean isOnlyBonusCustomers() {
        return onlyBonusCustomers;
    }

    public void setOnlyBonusCustomers(boolean onlyBonusCustomers) {
        this.onlyBonusCustomers = onlyBonusCustomers;
    }

    public ProductOffer toOffer() {
        return new ProductOffer(offerPercentage, start, end, onlyBonusCustomers);
    }
}
