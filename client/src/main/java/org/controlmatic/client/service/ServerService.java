package org.controlmatic.client.service;

import com.fasterxml.jackson.core.type.TypeReference;
import org.controlmatic.shared.domain.*;
import org.controlmatic.shared.request.*;
import org.controlmatic.shared.util.WebClient;

import java.util.List;

/**
 * Class for interacting with the server.
 */
public class ServerService extends WebClient {
    /**
     * Creates a new server service.
     *
     * @param endpoint the url to the server, e.g. http://localhost:8080
     */
    public ServerService(String endpoint) {
        super(endpoint);
    }

    // Customers

    /**
     * Gets all known customers.
     *
     * @return a list of customers
     * @throws WebClientException in case of errors
     */
    public List<Customer> getAllCustomers() throws WebClientException {
        return makeGET("/customers", new TypeReference<>() {});
    }

    /**
     * Gets a specific customer by id.
     *
     * @param id the customer id
     * @return the customer
     * @throws WebClientException in case of errors, or if a customer wasn't found
     */
    public Customer getCustomerById(int id) throws WebClientException {
        return makeGET("/customers/" + id, new TypeReference<>() {});
    }

    /**
     * Gets a specific customer by one of their bonus cards.
     *
     * @param number the number of the card
     * @param goodThruMonth the month the card expires
     * @param goodThruYear the year the card expires
     * @return the customer
     * @throws WebClientException in case of errors, or if a customer wasn't found
     */
    public Customer getCustomerByCard(String number, int goodThruMonth, int goodThruYear) throws WebClientException {
        return makeGET(String.format("/customers/%s/%d/%d", number, goodThruMonth, goodThruYear), new TypeReference<>() {});
    }

    /**
     * Searches for customers by name (first, last or both).
     *
     * @param name the name of the customer
     * @return a list of matching customers
     * @throws WebClientException in case of errors
     */
    public List<Customer> searchCustomers(String name) throws WebClientException {
        CustomerSearchRequest request = new CustomerSearchRequest();
        request.setName(name);
        return makePOST("/customers/search", request, new TypeReference<>() {});
    }

    // Products

    /**
     * Gets all products.
     *
     * @return a list of products
     * @throws WebClientException in case of errors
     */
    public List<Product> getAllProducts() throws WebClientException {
        return makeGET("/products", new TypeReference<>() {});
    }

    /**
     * Gets a product by its id.
     *
     * @param id the id of the product
     * @return the product
     * @throws WebClientException in case of errors, or if a product wasn't found
     */
    public Product getProductById(int id) throws WebClientException {
        return makeGET("/products/id/" + id, new TypeReference<>() {});
    }

    /**
     * Gets a product by its barcode.
     *
     * @param barcode the barcode for the product
     * @return the product
     * @throws WebClientException in case of errors, or if the product wasn't found
     */
    public Product getProductByBarcode(String barcode) throws WebClientException {
        return makeGET("/products/code/" + barcode, new TypeReference<>() {});
    }

    /**
     * Updates a product.
     *
     * @param id the id of the product to update
     * @param request a request object with fields that should be updated set (not set retains old values)
     * @return the updated product
     * @throws WebClientException in case of errors
     */
    public Product updateProduct(int id, ProductUpdateRequest request) throws WebClientException {
        return makePATCH("/products/id/" + id, request, new TypeReference<>() {});
    }

    /**
     * Searches for a product (containing name and keywords).
     *
     * @param keyword the search key word
     * @return a list of matching products
     * @throws WebClientException in case of errors
     */
    public List<Product> searchProducts(String keyword) throws WebClientException {
        ProductSearchRequest request = new ProductSearchRequest();
        request.setKeyword(keyword);
        return makePOST("/products/search", request, new TypeReference<>() {});
    }

    // Sales

    /**
     * Gets all sales.
     *
     * @return a list of sales.
     * @throws WebClientException in case of errors
     */
    public List<Sale> getAllSales() throws WebClientException {
        return makeGET("/sales", new TypeReference<>() {});
    }

    /**
     * Gets all sales by a specific customer.
     *
     * @param customerId the customer id to search sales for
     * @return a list of sales
     * @throws WebClientException in case of errors
     */
    public List<Sale> getSalesByCustomerId(int customerId) throws WebClientException {
        return makeGET("/sales/customer/" + customerId, new TypeReference<>() {});
    }

    /**
     * Gets all sales containing a specific product.
     *
     * @param productId the product id to search sales for
     * @return a list of sales
     * @throws WebClientException in case of errors
     */
    public List<Sale> getSalesByProductId(int productId) throws WebClientException {
        return makeGET("/sales/product/" + productId, new TypeReference<>() {});
    }

    /**
     * Creates a new sale.
     *
     * @param request a request object containing the new sale information
     * @return the created sale
     * @throws WebClientException in case of errors
     */
    public Sale createSale(SalesCreateRequest request) throws WebClientException {
        return makePOST("/sales", request, new TypeReference<>() {});
    }

    /**
     * Queries sales.
     *
     * @param request a request object containing query parameters (multiple fields set acts as AND)
     * @return a list of sales
     * @throws WebClientException in case of errors
     */
    public List<Sale> querySales(SalesQueryRequest request) throws WebClientException {
        return makePOST("/sales/query", request, new TypeReference<>() {});
    }
}
