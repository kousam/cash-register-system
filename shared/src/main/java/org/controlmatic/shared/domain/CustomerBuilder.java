package org.controlmatic.shared.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Builder class for making {@link Customer} objects.
 */
public class CustomerBuilder {
    private int id;
    private String firstName;
    private String lastName;
    private LocalDateTime birthDate;
    private CustomerAddress address;
    private List<BonusCard> bonusCards = Collections.emptyList();
    private BigDecimal bonusPoints = BigDecimal.ZERO;
    private String sex;

    public CustomerBuilder id(int id) {
        this.id = id;
        return this;
    }

    public CustomerBuilder firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public CustomerBuilder lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public CustomerBuilder birthDate(LocalDateTime birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public CustomerBuilder address(CustomerAddress address) {
        this.address = address;
        return this;
    }

    public CustomerBuilder bonusCards(List<BonusCard> bonusCards) {
        this.bonusCards = bonusCards;
        return this;
    }

    public CustomerBuilder bonusPoints(BigDecimal bonusPoints) {
        this.bonusPoints = bonusPoints;
        return this;
    }

    public CustomerBuilder sex(String sex) {
        this.sex = sex;
        return this;
    }

    public Customer build() {
        return new Customer(id, firstName, lastName, birthDate, address, bonusCards, bonusPoints, sex);
    }
}
