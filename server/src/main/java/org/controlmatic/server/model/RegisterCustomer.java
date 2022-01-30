package org.controlmatic.server.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.controlmatic.shared.domain.BonusCard;
import org.controlmatic.shared.domain.CustomerAddress;
import org.controlmatic.shared.domain.CustomerBuilder;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Class for representing the data in an external Customer Register.
 */
public class RegisterCustomer {
    private final int id;
    private final int optLockVersion;
    private final String firstName;
    private final String lastName;
    private final OffsetDateTime birthDate;
    private final CustomerAddress address;
    private final List<BonusCard> bonusCards;
    private final String sex;

    public RegisterCustomer() {
        this.id = 0;
        this.optLockVersion = 0;
        this.firstName = null;
        this.lastName = null;
        this.birthDate = null;
        this.address = null;
        this.bonusCards = Collections.emptyList();
        this.sex = null;
    }

    @JacksonXmlProperty(localName = "customerNo")
    public int getId() {
        return id;
    }

    public int getOptLockVersion() {
        return optLockVersion;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public OffsetDateTime getBirthDate() {
        return birthDate;
    }

    public CustomerAddress getAddress() {
        return address;
    }

    @JacksonXmlProperty(localName = "bonusCard")
    @JacksonXmlElementWrapper(useWrapping = false)
    public List<BonusCard> getBonusCards() {
        return bonusCards;
    }

    public String getSex() {
        return sex;
    }

    /**
     * Sets fields in a {@link CustomerBuilder} with data from this object.
     *
     * @param builder the builder to set fields in
     */
    public void decorate(CustomerBuilder builder) {
        builder.id(id).firstName(firstName).lastName(lastName).birthDate(birthDate != null ? birthDate.toLocalDateTime() : null).address(address).bonusCards(bonusCards).sex(sex);
    }
}
