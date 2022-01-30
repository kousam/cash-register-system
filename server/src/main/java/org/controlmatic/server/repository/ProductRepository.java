package org.controlmatic.server.repository;

import org.controlmatic.server.model.entity.ProductEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Database repository for storing product information.
 */
public interface ProductRepository extends CrudRepository<ProductEntity, Integer> {
}
