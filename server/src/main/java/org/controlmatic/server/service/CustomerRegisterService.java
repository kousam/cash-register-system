package org.controlmatic.server.service;

import com.fasterxml.jackson.core.type.TypeReference;
import org.controlmatic.server.model.RegisterCustomer;
import org.controlmatic.shared.domain.WebClientException;
import org.controlmatic.shared.util.WebClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Spring service for interacting with an external Customer Register.
 */
@Service
public class CustomerRegisterService extends WebClient {
    public CustomerRegisterService(@Value("${controlmatic.customer-register.url}") String endpoint) {
        super(endpoint);
    }

    /**
     * Gets a customer from the register by id.
     *
     * @param id the id of the customer
     * @return the customer
     * @throws WebClientException in case of errors, or if the customer doesn't exist
     */
    public RegisterCustomer getCustomerById(int id) throws WebClientException {
        return makeGET("/rest/findByCustomerNo/" + id, new TypeReference<>() {});
    }

    /**
     * Gets a customer from the register by one of their registered bonus cards.
     *
     * @param number the number of the card
     * @param goodThruYear the year it expires
     * @param goodThruMonth the month it expires
     * @return the customer
     * @throws WebClientException in case of errors, or if the customer doesn't exist
     */
    public RegisterCustomer getCustomerByBonusCard(String number, int goodThruYear, int goodThruMonth) throws WebClientException {
        return makeGET(String.format("/rest/findByBonusCard/%s/%d/%d", number, goodThruYear, goodThruMonth), new TypeReference<>() {});
    }
}
