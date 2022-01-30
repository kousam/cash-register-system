package org.controlmatic.shared.domain;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * Builder class for making {@link Product} objects
 */
public class ProductBuilder {
    private int id;
    private String name;
    private BigDecimal price;
    private int vatPercentage;
    private String barcode;
    private List<String> keywords = Collections.emptyList();
    private List<ProductOffer> offers = Collections.emptyList();

    public ProductBuilder id(int id) {
        this.id = id;
        return this;
    }

    public ProductBuilder name(String name) {
        this.name = name;
        return this;
    }

    public ProductBuilder price(BigDecimal price) {
        this.price = price;
        return this;
    }

    public ProductBuilder vatPercentage(int vatPercentage) {
        this.vatPercentage = vatPercentage;
        return this;
    }

    public ProductBuilder barcode(String barcode) {
        this.barcode = barcode;
        return this;
    }

    public ProductBuilder keywords(List<String> keywords) {
        this.keywords = keywords;
        return this;
    }

    public ProductBuilder offers(List<ProductOffer> offers) {
        this.offers = offers;
        return this;
    }

    public Product build() {
        return new Product(id, name, price, vatPercentage, barcode, keywords, offers);
    }
}
