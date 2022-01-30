package org.controlmatic.server.repository;

import org.controlmatic.server.model.entity.SaleEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Database repository for storing sales.
 */
public interface SaleRepository extends CrudRepository<SaleEntity, Integer> {
    /**
     * Returns all sales made by a specific customer.
     *
     * @param customerId the customer id to get sales by
     * @return a list of sales
     */
    List<SaleEntity> findByCustomerId(int customerId);

    /**
     * Returns all sales within a specific time period.
     *
     * @param start the start date
     * @param end the end date
     * @return a list of sales
     */
    List<SaleEntity> findByTimestampBetween(LocalDateTime start, LocalDateTime end);

    /**
     * Returns all sales that contains a specific product.
     *
     * @param id the id of the product
     * @return a list of sales
     */
    @Query("SELECT s FROM SaleEntity s JOIN s.products p WHERE p.id = :id")
    List<SaleEntity> findByProductsContainingId(int id);
}
