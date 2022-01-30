package org.controlmatic.server.service;

import org.controlmatic.server.model.CatalogProduct;
import org.controlmatic.server.model.entity.ProductEntity;
import org.controlmatic.server.repository.ProductRepository;
import org.controlmatic.shared.domain.Product;
import org.controlmatic.shared.domain.ProductBuilder;
import org.controlmatic.shared.domain.WebClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Spring service for dealing with {@link Product}, backed by an in-memory cache.
 */
@Service
public class ProductService {
    private final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;
    private final ProductCatalogService productCatalogService;

    private final Map<Integer, Product> productCache = new ConcurrentHashMap<>();

    public ProductService(ProductRepository productRepository, ProductCatalogService productCatalogService) {
        this.productRepository = productRepository;
        this.productCatalogService = productCatalogService;

        new CacheThread().start();
    }

    public List<Product> getAllProducts() {
        return productCache.values().stream().toList();
    }

    public Optional<Product> getProductById(int id) {
        return Optional.ofNullable(productCache.get(id));
    }

    public Optional<Product> getProductByBarcode(String barcode) {
        return productCache.values().stream().filter(p -> p.getBarcode().equals(barcode)).findFirst();
    }

    public List<Product> searchProducts(String word) {
        String keyword = word.toLowerCase();
        return getAllProducts().stream().filter(product -> {
            // Search name
            if (product.getName().toLowerCase().contains(keyword)) {
                return true;
            }
            // Search keywords
            else if (product.getKeywords().stream().anyMatch(k -> k.toLowerCase().contains(keyword))) {
                return true;
            }
            return false;
        }).toList();
    }

    private class CacheThread extends Thread {
        private static final long WORKER_POLLING_INTERVAL = 5000;

        public CacheThread() {
            setDaemon(true);
        }

        public void run() {
            try {
                while (!interrupted()) {
                    // Fetch all known products
                    Map<Integer, ProductEntity> dbProducts = new HashMap<>();
                    productRepository.findAll().forEach(p -> dbProducts.put(p.getId(), p));

                    try {
                        // Fetch all products in the catalog
                        List<CatalogProduct> catalogProducts = productCatalogService.getAllProducts();
                        Set<Integer> catalogProductIds = new HashSet<>();

                        // Loop through all products in the catalog
                        for (CatalogProduct catalogProduct : catalogProducts) {
                            catalogProductIds.add(catalogProduct.getId());

                            ProductBuilder builder = new ProductBuilder();
                            catalogProduct.decorate(builder);

                            // Check if database contains additional information
                            if (dbProducts.containsKey(catalogProduct.getId())) {
                                dbProducts.get(catalogProduct.getId()).decorate(builder);
                            }
                            else {
                                builder.offers(Collections.emptyList());
                            }

                            // Update cache
                            productCache.put(catalogProduct.getId(), builder.build());
                        }

                        // Prune removed products from cache and database
                        for (Integer productId : Stream.concat(productCache.keySet().stream(), dbProducts.keySet().stream()).collect(Collectors.toSet())) {
                            if (!catalogProductIds.contains(productId)) {
                                productCache.remove(productId);
                                if (productRepository.existsById(productId)) {
                                    productRepository.deleteById(productId);
                                }

                                logger.debug("Removed product {} from cache and database", productId);
                            }
                        }
                    }
                    catch (WebClientException e) {
                        logger.warn("Failed to fetch products", e);
                    }

                    Thread.sleep(WORKER_POLLING_INTERVAL);
                }
            }
            catch (InterruptedException ignored) {
            }
        }
    }
}
