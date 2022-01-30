package org.controlmatic.shared.domain;

/**
 * Class for representing the address of a {@link Customer}.
 * Be careful when modifying this, as it's used in RegisterCustomer and could break external systems.
 */
public class CustomerAddress {
    private final String streetAddress;
    private final int postalCode;
    private final String postOffice;
    private final String country;

    /**
     * Creates a new {@link CustomerAddress}.
     *
     * @param streetAddress the street address
     * @param postalCode the postal code
     * @param postOffice the post office
     * @param country the country
     */
    public CustomerAddress(String streetAddress, int postalCode, String postOffice, String country) {
        this.streetAddress = streetAddress;
        this.postalCode = postalCode;
        this.postOffice = postOffice;
        this.country = country;
    }

    /**
     * Empty constructor for serialization.
     */
    public CustomerAddress() {
        this.streetAddress = null;
        this.postalCode = 0;
        this.postOffice = null;
        this.country = null;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public String getPostOffice() {
        return postOffice;
    }

    public String getCountry() {
        return country;
    }
}
