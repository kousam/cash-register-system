package org.controlmatic.shared.request;

/**
 * Request object for searching for products.
 */
public class ProductSearchRequest {
    private String keyword;

    /**
     * Default constructor, use setters to modify the request.
     */
    public ProductSearchRequest() {
        this.keyword = null;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
