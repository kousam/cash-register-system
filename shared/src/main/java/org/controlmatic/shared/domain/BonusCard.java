package org.controlmatic.shared.domain;

/**
 * Class for representing a bonus card.
 * Be careful when modifying this, as it's used in RegisterCustomer and could break external systems.
 */
public class BonusCard {
    private final int id;
    private final String number;
    private final int goodThruMonth;
    private final int goodThruYear;
    private final boolean blocked;
    private final boolean expired;
    private final String holderName;

    /**
     * Creates a new {@link BonusCard}.
     *
     * @param id the id of the card
     * @param number the number of the card
     * @param goodThruMonth the month the card expires
     * @param goodThruYear the year the card expires
     * @param blocked whether the card is blocked from use
     * @param expired whether the card is expired
     * @param holderName the cardholder name
     */
    public BonusCard(int id, String number, int goodThruMonth, int goodThruYear, boolean blocked, boolean expired, String holderName) {
        this.id = id;
        this.number = number;
        this.goodThruMonth = goodThruMonth;
        this.goodThruYear = goodThruYear;
        this.blocked = blocked;
        this.expired = expired;
        this.holderName = holderName;
    }

    /**
     * Empty constructor for serialization.
     */
    public BonusCard() {
        this.id = 0;
        this.number = null;
        this.goodThruMonth = 0;
        this.goodThruYear = 0;
        this.blocked = false;
        this.expired = false;
        this.holderName = null;
    }

    public int getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public int getGoodThruMonth() {
        return goodThruMonth;
    }

    public int getGoodThruYear() {
        return goodThruYear;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public boolean isExpired() {
        return expired;
    }

    public String getHolderName() {
        return holderName;
    }
}
