package org.controlmatic.server.controller;

import org.controlmatic.server.model.entity.CustomerEntity;
import org.controlmatic.server.model.entity.SaleEntity;
import org.controlmatic.server.model.entity.SaleProductEntity;
import org.controlmatic.server.repository.CustomerRepository;
import org.controlmatic.server.repository.SaleRepository;
import org.controlmatic.server.service.CustomerService;
import org.controlmatic.server.service.ProductService;
import org.controlmatic.shared.domain.*;
import org.controlmatic.shared.request.SalesCreateRequest;
import org.controlmatic.shared.request.SalesQueryRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * API routes for {@link Sale} operations.
 */
@RestController
@RequestMapping(value = "/sales")
public class SaleController {
    private final SaleRepository saleRepository;
    private final CustomerRepository customerRepository;
    private final CustomerService customerService;
    private final ProductService productService;

    public SaleController(SaleRepository saleRepository, CustomerRepository customerRepository, CustomerService customerService, ProductService productService) {
        this.saleRepository = saleRepository;
        this.customerRepository = customerRepository;
        this.customerService = customerService;
        this.productService = productService;
    }

    private Sale convertToSale(SaleEntity saleEntity) {
        // Fetch product info
        List<SaleProduct> saleProducts = saleEntity.getProducts().stream().map(sp -> {
            Product product = productService.getProductById(sp.getId()).orElse(new ProductBuilder().name("DELETED PRODUCT").price(BigDecimal.ZERO).build());
            return new SaleProduct(sp.getId(), sp.getAmount(), product.getName());
        }).collect(Collectors.toList());

        return new Sale(saleEntity.getId(), saleEntity.getCustomerId(), saleProducts, saleEntity.getTotalPrice(), saleEntity.getTimestamp());
    }

    @GetMapping
    public List<Sale> getAllSales() {
        return StreamSupport.stream(saleRepository.findAll().spliterator(), false).map(this::convertToSale).toList();
    }

    @GetMapping(value = "/customer/{id}")
    public List<Sale> getSalesByCustomerId(@PathVariable int id) {
        return saleRepository.findByCustomerId(id).stream().map(this::convertToSale).toList();
    }

    @GetMapping(value = "/product/{id}")
    public List<Sale> getSalesByProductId(@PathVariable int id) {
        // Get all sales that contains the specified product id
        return saleRepository.findByProductsContainingId(id).stream().map(this::convertToSale).toList();
    }

    @PostMapping
    public Sale createSale(@RequestBody SalesCreateRequest request) {
        // Validate request
        if (request.getProducts() == null || request.getProducts().size() == 0 || request.getProducts().stream().anyMatch(p -> p.getId() == 0 || p.getAmount() == 0)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // Save sale
        SaleEntity savedSale = saleRepository.save(new SaleEntity(request.getCustomerId(), request.getProducts().stream().map(SaleProductEntity::new).collect(Collectors.toList()), request.getTotalPrice(), request.getTimestamp()));

        // Award bonus points
        if (request.getCustomerId() != 0) {
            // Lookup existing customer, or add new if it doesn't exist
            CustomerEntity customer = customerRepository.findById(request.getCustomerId()).orElse(new CustomerEntity(request.getCustomerId(), new BigDecimal(0)));
            customer.addBonusPoints(request.getTotalPrice().multiply(new BigDecimal("0.01")));
            customerRepository.save(customer);
        }

        return convertToSale(savedSale);
    }

    @PostMapping(value = "/query")
    public List<Sale> querySales(@RequestBody SalesQueryRequest request) {
        // Get all sales within specified time period
        List<SaleEntity> sales = saleRepository.findByTimestampBetween(request.getDateTimeStart(), request.getDateTimeEnd());

        // Check if we need to fetch customers
        if (request.hasCustomerRequirements()) {
            List<Customer> customers = customerService.getCustomersByAgeAndSex(request.getCustomerAgeStart(), request.getCustomerAgeEnd(), request.getCustomerSex());
            List<Integer> customerIds = customers.stream().map(Customer::getId).toList();

            // Remove all sales that doesn't match customers
            sales.removeIf(s -> !customerIds.contains(s.getCustomerId()));
        }

        return sales.stream().map(this::convertToSale).toList();
    }
}
