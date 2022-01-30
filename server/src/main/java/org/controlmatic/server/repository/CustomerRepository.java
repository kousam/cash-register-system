package org.controlmatic.server.repository;

import org.controlmatic.server.model.entity.CustomerEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Database repository for storing customer information.
 */
public interface CustomerRepository extends CrudRepository<CustomerEntity, Integer> {
}
