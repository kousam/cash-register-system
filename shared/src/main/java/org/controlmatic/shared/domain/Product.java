package org.controlmatic.shared.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * Class for representing a product.
 */
public class Product {
    private final int id;
    private final String name;
    private final BigDecimal price;
    private final int vatPercentage;
    private final String barcode;
    private final List<String> keywords;
    private final List<ProductOffer> offers;

    /**
     * Creates a new {@link Product}.
     *
     * @param id the unique id of the product
     * @param name the name of the product
     * @param price the price of the product
     * @param vatPercentage the VAT percentage (e.g. 24)
     * @param barcode the barcode for the product
     * @param keywords keywords describing the product
     * @param offers any offers for this product
     */
    public Product(int id, String name, BigDecimal price, int vatPercentage, String barcode, List<String> keywords, List<ProductOffer> offers) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.vatPercentage = vatPercentage;
        this.barcode = barcode;
        this.keywords = keywords;
        this.offers = offers;
    }

    /**
     * Empty constructor for serialization.
     */
    public Product() {
        this.id = 0;
        this.name = null;
        this.price = null;
        this.vatPercentage = 0;
        this.barcode = null;
        this.keywords = Collections.emptyList();
        this.offers = Collections.emptyList();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getVatPercentage() {
        return vatPercentage;
    }

    public String getBarcode() {
        return barcode;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public List<ProductOffer> getOffers() {
        return offers;
    }

    @JsonIgnore
    public ProductBuilder builder() {
        return new ProductBuilder().id(id).name(name).price(price).vatPercentage(vatPercentage).barcode(barcode).keywords(keywords).offers(offers);
    }
}
