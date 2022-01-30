package org.controlmatic.shared.request;

/**
 * Request object for searching for customers by name.
 */
public class CustomerSearchRequest {
    private String name;

    /**
     * Default constructor, use setters to modify the request.
     */
    public CustomerSearchRequest() {
        this.name = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String keyword) {
        this.name = keyword;
    }
}
