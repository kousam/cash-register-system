package org.controlmatic.client.model;

/**
 * Class for representing the result from a card reader swipe.
 */
public class CardReaderResult {
    private final String paymentCardNumber;
    private final PaymentCardState paymentState;
    private final PaymentCardType paymentCardType;

    private final String bonusCardNumber;
    private final BonusCardState bonusState;
    private final int goodThruMonth;
    private final int goodThruYear;

    /**
     * Empty constructor for serialization
     */
    public CardReaderResult() {
        paymentCardNumber = null;
        paymentState = null;
        paymentCardType = null;

        bonusCardNumber = null;
        bonusState = null;
        goodThruMonth = 0;
        goodThruYear = 0;
    }

    public String getPaymentCardNumber() {
        return paymentCardNumber;
    }

    public PaymentCardState getPaymentState() {
        return paymentState;
    }

    public PaymentCardType getPaymentCardType() {
        return paymentCardType;
    }

    public String getBonusCardNumber() {
        return bonusCardNumber;
    }

    public BonusCardState getBonusState() {
        return bonusState;
    }

    public int getGoodThruMonth() {
        return goodThruMonth;
    }

    public int getGoodThruYear() {
        return goodThruYear;
    }

    public boolean IsPaymentCard() {
        return paymentCardNumber != null && paymentState != null && paymentCardType != null;
    }

    public boolean IsBonusCard() {
        return bonusCardNumber != null && bonusState != null;
    }

    public boolean IsCombinedCard() {
        return IsPaymentCard() && IsBonusCard();
    }

    /**
     * Payment card states.
     */
    public enum PaymentCardState {
        ACCEPTED,
        INSUFFICIENT_FUNDS,
        UNSUPPORTED_CARD,
        INVALID_PIN,
        NETWORK_ERROR
    }

    /**
     * Payment card types.
     */
    public enum PaymentCardType {
        CREDIT,
        DEBIT
    }

    /**
     * Bonus card states.
     */
    public enum BonusCardState {
        ACCEPTED,
        UNSUPPORTED_CARD
    }
}
