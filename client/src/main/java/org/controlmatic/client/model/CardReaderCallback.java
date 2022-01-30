package org.controlmatic.client.model;

/**
 * Class for representing the callback that is fired when Card Reader state changes.
 */
public class CardReaderCallback {
    private final CardReaderStatus status;
    private final CardReaderResult result;

    public CardReaderCallback(CardReaderStatus status, CardReaderResult result) {
        this.status = status;
        this.result = result;
    }

    /**
     * Gets the current status.
     *
     * @return card reader status
     */
    public CardReaderStatus getStatus() {
        return status;
    }

    /**
     * Gets the current result. Only available when {@link #getStatus()} is {@link CardReaderStatus#DONE}, otherwise null.
     *
     * @return card reader result, or null
     */
    public CardReaderResult getResult() {
        return result;
    }
}
