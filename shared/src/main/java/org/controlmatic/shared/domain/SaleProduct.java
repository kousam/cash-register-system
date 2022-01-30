package org.controlmatic.shared.domain;

/**
 * Class for representing a {@link Product} with some additional information in a {@link Sale}.
 */
public class SaleProduct {
    private final int id;
    private final int amount;
    private final String name;

    /**
     * Creates a new {@link SaleProduct}.
     *
     * @param id the product id
     * @param amount the amount of product(s)
     * @param name the name of the product
     */
    public SaleProduct(int id, int amount, String name) {
        this.id = id;
        this.amount = amount;
        this.name = name;
    }

    /**
     * Creates a new {@link SaleProduct} without name and price.
     *
     * @param id the product id
     * @param amount the amount of product(s)
     */
    public SaleProduct(int id, int amount) {
        this(id, amount, null);
    }

    /**
     * Empty constructor for serialization.
     */
    public SaleProduct() {
        this.id = 0;
        this.amount = 0;
        this.name = null;
    }

    public int getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    public String getName() {
        return name;
    }
}
