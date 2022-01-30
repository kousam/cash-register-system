package org.controlmatic.shared.request;

import org.controlmatic.shared.domain.ProductOffer;

import java.math.BigDecimal;
import java.util.List;

/**
 * Request object for updating a product.
 */
public class ProductUpdateRequest {
    private BigDecimal price;
    private List<ProductOffer> offers;

    /**
     * Default constructor, use setters to modify the request.
     */
    public ProductUpdateRequest() {
        this.price = null;
        this.offers = null;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public List<ProductOffer> getOffers() {
        return offers;
    }

    public void setOffers(List<ProductOffer> offers) {
        this.offers = offers;
    }
}
