package org.controlmatic.server.service;

import com.fasterxml.jackson.core.type.TypeReference;
import org.controlmatic.shared.domain.WebClientException;
import org.controlmatic.shared.util.WebClient;
import org.controlmatic.server.model.CatalogProduct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Spring service for interacting with an external Product Catalog.
 */
@Service
public class ProductCatalogService extends WebClient {
    public ProductCatalogService(@Value("${controlmatic.product-catalog.url}") String endpoint) {
        super(endpoint);
    }

    /**
     * Gets a product from the catalog by barcode.
     *
     * @param barcode the barcode for the product
     * @return the product
     * @throws WebClientException in case of errors, or if the product doesn't exist
     */
    public CatalogProduct getProductByBarcode(String barcode) throws WebClientException {
        return makeGET("/rest/findByBarCode/" + UriUtils.encodePath(barcode, StandardCharsets.UTF_8), new TypeReference<>() {});
    }

    /**
     * Gets all products with a keyword.
     *
     * @param keyword the keyword for which to find products
     * @return a list of products
     * @throws WebClientException in case of errors
     */
    public List<CatalogProduct> getProductsByKeyword(String keyword) throws WebClientException {
        return makeGET("/rest/findByKeyword/" + UriUtils.encodePath(keyword, StandardCharsets.UTF_8), new TypeReference<>() {});
    }

    /**
     * Gets all products matching a name.
     *
     * @param name the name for which to find products
     * @return a list of products
     * @throws WebClientException in case of errors
     */
    public List<CatalogProduct> getProductsByName(String name) throws WebClientException {
        return makeGET("/rest/findByName/" + UriUtils.encodePath(name, StandardCharsets.UTF_8), new TypeReference<>() {});
    }

    /**
     * Gets all available products.
     *
     * @return a list of all products
     * @throws WebClientException in case of errors
     */
    public List<CatalogProduct> getAllProducts() throws WebClientException {
        // Not sure if this is intended, but it works!
        return getProductsByName("*");
    }
}
