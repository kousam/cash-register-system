package org.controlmatic.shared.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Class for representing a customer.
 */
public class Customer {
    private final int id;
    private final String firstName;
    private final String lastName;
    private final LocalDateTime birthDate;
    private final CustomerAddress address;
    private final List<BonusCard> bonusCards;
    private final BigDecimal bonusPoints;
    private final String sex;

    /**
     * Creates a new {@link Customer}.
     *
     * @param id the id of the customer
     * @param firstName the first name of the customer
     * @param lastName the last name of the customer
     * @param birthDate the birthdate of the customer
     * @param address the address of the customer
     * @param bonusCards any bonus cards the customer has
     * @param bonusPoints the amount of bonus points the customer has
     * @param sex the sex of the customer
     */
    public Customer(int id, String firstName, String lastName, LocalDateTime birthDate, CustomerAddress address, List<BonusCard> bonusCards, BigDecimal bonusPoints, String sex) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.address = address;
        this.bonusCards = bonusCards;
        this.bonusPoints = bonusPoints;
        this.sex = sex;
    }

    /**
     * Empty constructor for serialization.
     */
    public Customer() {
        this.id = 0;
        this.firstName = null;
        this.lastName = null;
        this.birthDate = null;
        this.address = new CustomerAddress();
        this.bonusCards = Collections.emptyList();
        this.bonusPoints = null;
        this.sex = null;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDateTime getBirthDate() {
        return birthDate;
    }

    public CustomerAddress getAddress() {
        return address;
    }

    public List<BonusCard> getBonusCards() {
        return bonusCards;
    }

    public BigDecimal getBonusPoints() {
        return bonusPoints;
    }

    public String getSex() {
        return sex;
    }
}
