package org.controlmatic.server.controller;

import org.controlmatic.server.model.entity.ProductEntity;
import org.controlmatic.server.model.entity.ProductOfferEntity;
import org.controlmatic.server.repository.ProductRepository;
import org.controlmatic.server.service.ProductService;
import org.controlmatic.shared.domain.Product;
import org.controlmatic.shared.domain.ProductBuilder;
import org.controlmatic.shared.request.ProductSearchRequest;
import org.controlmatic.shared.request.ProductUpdateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * API routes for {@link Product} operations.
 */
@RestController
@RequestMapping(value = "/products")
public class ProductController {
    private final ProductRepository productRepository;
    private final ProductService productService;

    public ProductController(ProductRepository productRepository, ProductService productService) {
        this.productRepository = productRepository;
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping(value = "/id/{id}")
    public Product getProductById(@PathVariable int id) {
        return productService.getProductById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping(value = "/code/{barcode}")
    public Product getProductByBarcode(@PathVariable String barcode) {
        return productService.getProductByBarcode(barcode).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PatchMapping(value = "/id/{id}")
    public Product updateProductById(@PathVariable int id, @RequestBody ProductUpdateRequest request) {
        // Find existing product
        ProductEntity productEntity = productRepository.findById(id).orElse(new ProductEntity(id, null, null));
        ProductBuilder builder = productService.getProductById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)).builder();

        // Update product
        if (request.getPrice() != null) {
            productEntity.setPrice(request.getPrice());
        }
        if (request.getOffers() != null) {
            productEntity.setOffers(request.getOffers().stream().map(ProductOfferEntity::new).collect(Collectors.toList()));
        }
        productRepository.save(productEntity);
        productEntity.decorate(builder);

        // Return updated product
        return builder.build();
    }

    @PostMapping(value = "/search")
    public List<Product> searchProducts(@RequestBody ProductSearchRequest request) {
        return productService.searchProducts(request.getKeyword());
    }
}
