package org.controlmatic.server.model.entity;

import org.controlmatic.shared.domain.CustomerBuilder;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

/**
 * Class for additional customer information.
 */
@Entity
public class CustomerEntity {
    @Id
    private Integer id;

    private BigDecimal bonusPoints;

    protected CustomerEntity() {}

    public CustomerEntity(Integer id, BigDecimal bonusPoints) {
        this.id = id;
        this.bonusPoints = bonusPoints;
    }

    public Integer getId() {
        return id;
    }

    public BigDecimal getBonusPoints() {
        return bonusPoints;
    }

    public void addBonusPoints(BigDecimal bonusPoints) {
        this.bonusPoints = this.bonusPoints.add(bonusPoints);
    }

    /**
     * Sets fields in a {@link CustomerBuilder} with data from this object.
     *
     * @param builder the builder to set fields in
     */
    public void decorate(CustomerBuilder builder) {
        builder.id(id).bonusPoints(bonusPoints);
    }
}
