package org.controlmatic.shared.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

/**
 * Class for representing a special offer for a {@link Product}.
 */
public class ProductOffer {
    private final int offerPercentage;
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final boolean onlyBonusCustomers;

    /**
     * Creates a new {@link ProductOffer}.
     *
     * @param offerPercentage the discount percentage
     * @param start when the offer starts, or null
     * @param end when the offer ends, or null if indefinite
     * @param onlyBonusCustomers whether the offer is only for bonus customers
     */
    public ProductOffer(int offerPercentage, LocalDateTime start, LocalDateTime end, boolean onlyBonusCustomers) {
        this.offerPercentage = offerPercentage;
        this.start = start;
        this.end = end;
        this.onlyBonusCustomers = onlyBonusCustomers;
    }

    /**
     * Empty constructor for serialization.
     */
    public ProductOffer() {
        this.offerPercentage = 0;
        this.start = null;
        this.end = null;
        this.onlyBonusCustomers = false;
    }

    public int getOfferPercentage() {
        return offerPercentage;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public boolean isOnlyBonusCustomers() {
        return onlyBonusCustomers;
    }

    @JsonIgnore
    public boolean isActive() {
        LocalDateTime now = LocalDateTime.now();
        return (start == null || now.isAfter(start)) && (end == null || now.isBefore(end));
    }
}
