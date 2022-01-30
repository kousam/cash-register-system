package org.controlmatic.server.service;

import org.controlmatic.server.model.RegisterCustomer;
import org.controlmatic.server.model.entity.CustomerEntity;
import org.controlmatic.server.repository.CustomerRepository;
import org.controlmatic.shared.domain.Customer;
import org.controlmatic.shared.domain.CustomerBuilder;
import org.controlmatic.shared.domain.WebClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Spring service for dealing with {@link Customer}, backed by an in-memory cache.
 */
@Service
public class CustomerService {
    private final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    private final CustomerRepository customerRepository;
    private final CustomerRegisterService customerRegisterService;

    private final Map<Integer, Customer> customerCache = new ConcurrentHashMap<>();

    public CustomerService(CustomerRepository customerRepository, CustomerRegisterService customerRegisterService) {
        this.customerRepository = customerRepository;
        this.customerRegisterService = customerRegisterService;

        new CacheThread().start();
    }

    public List<Customer> getAllCustomers() {
        return customerCache.values().stream().toList();
    }

    public Optional<Customer> getCustomerById(int id) {
        // Return from cache if available
        if (customerCache.containsKey(id)) {
            return Optional.of(customerCache.get(id));
        }

        // Check if it exists in the register
        try {
            RegisterCustomer registerCustomer = customerRegisterService.getCustomerById(id);
            CustomerBuilder builder = new CustomerBuilder();
            registerCustomer.decorate(builder);

            return Optional.of(builder.build());
        }
        catch (WebClientException e) {
            return Optional.empty();
        }
    }

    public Optional<Customer> getCustomerByBonusCard(String number, int goodThruMonth, int goodThruYear) {
        // Search cache
        Optional<Customer> customer = customerCache.values().stream().filter(c -> c.getBonusCards().stream().anyMatch(card -> card.getNumber().equals(number) && card.getGoodThruMonth() == goodThruMonth && card.getGoodThruYear() == goodThruYear)).findFirst();
        if (customer.isPresent()) {
            return customer;
        }

        // Check if it exists in the register
        try {
            RegisterCustomer registerCustomer = customerRegisterService.getCustomerByBonusCard(number, goodThruYear, goodThruMonth);
            CustomerBuilder builder = new CustomerBuilder();
            registerCustomer.decorate(builder);

            return Optional.of(builder.build());
        }
        catch (WebClientException e) {
            return Optional.empty();
        }
    }

    public List<Customer> getCustomersByAgeAndSex(int ageStart, int ageEnd, String sex) {
        LocalDate now = LocalDate.now();
        return customerCache.values().stream().filter(customer -> {
            int customerAge = Period.between(customer.getBirthDate().toLocalDate(), now).getYears();
            if (ageStart != 0 && customerAge < ageStart) {
                return false;
            }
            if (ageEnd != 0 && customerAge > ageEnd) {
                return false;
            }
            if (sex != null && !sex.equals(customer.getSex())) {
                return false;
            }
            return true;
        }).toList();
    }

    public List<Customer> searchCustomers(String name) {
        return customerCache.values().stream().filter(customer -> (customer.getFirstName() + " " + customer.getLastName()).toLowerCase().contains(name.toLowerCase())).toList();
    }

    private class CacheThread extends Thread {
        private static final long WORKER_POLLING_INTERVAL = 5000;

        public CacheThread() {
            setDaemon(true);
        }

        public void run() {
            try {
                while (!interrupted()) {
                    // Loop through all known customers
                    for (CustomerEntity customerEntity : customerRepository.findAll()) {
                        try {
                            CustomerBuilder builder = new CustomerBuilder();
                            customerEntity.decorate(builder);

                            // Query customer register for information
                            RegisterCustomer registerCustomer = customerRegisterService.getCustomerById(customerEntity.getId());
                            registerCustomer.decorate(builder);

                            // Update cache
                            customerCache.put(customerEntity.getId(), builder.build());
                        }
                        catch (WebClientException e) {
                            if (e.getStatusCode().isEmpty() || e.getStatusCode().get() != 404) {
                                logger.warn("Failed to fetch customer data for " + customerEntity.getId(), e);
                            }
                            else if (e.getStatusCode().get() == 404) {
                                // Remove from cache and database if removed from register
                                customerCache.remove(customerEntity.getId());
                                customerRepository.delete(customerEntity);

                                logger.debug("Removed customer {} from cache and database", customerEntity.getId());
                            }
                        }
                    }

                    Thread.sleep(WORKER_POLLING_INTERVAL);
                }
            }
            catch (InterruptedException ignored) {
            }
        }
    }
}
