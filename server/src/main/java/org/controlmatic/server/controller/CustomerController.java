package org.controlmatic.server.controller;

import org.controlmatic.server.service.CustomerService;
import org.controlmatic.shared.domain.Customer;
import org.controlmatic.shared.request.CustomerSearchRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * API routes for {@link Customer} operations.
 */
@RestController
@RequestMapping(value = "/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping(value = "/{id}")
    public Customer getCustomerById(@PathVariable int id) {
        return customerService.getCustomerById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping(value = "/{number}/{month}/{year}")
    public Customer getCustomerByCard(@PathVariable String number, @PathVariable int month, @PathVariable int year) {
        return customerService.getCustomerByBonusCard(number, month, year).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping(value = "/search")
    public List<Customer> searchCustomers(@RequestBody CustomerSearchRequest request) {
        return customerService.searchCustomers(request.getName());
    }
}
