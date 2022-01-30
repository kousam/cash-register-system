package org.controlmatic.client.service;

import com.fasterxml.jackson.core.type.TypeReference;
import org.controlmatic.client.model.CardReaderCallback;
import org.controlmatic.client.model.CardReaderResult;
import org.controlmatic.client.model.CardReaderStatus;
import org.controlmatic.shared.domain.WebClientException;
import org.controlmatic.shared.util.WebClient;

import java.math.BigDecimal;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Class for interacting with an external Card Reader.
 */
public class CardReaderService extends WebClient {
    /**
     * Create a new card reader service.
     *
     * @param endpoint the url to the card reader, e.g. http://localhost:9002
     */
    public CardReaderService(String endpoint) {
        super(endpoint);
    }

    /**
     * Create a new card reader service with an automatic poller.
     *
     * @param endpoint the url to the card reader, e.g. http://localhost:9002
     * @param callback a callback for reader events
     */
    public CardReaderService(String endpoint, Consumer<CardReaderCallback> callback) {
        this(endpoint);

        new WorkerThread(callback).start();
    }

    /**
     * Resets the reader to the IDLE state, regardless of which state it's currently in.
     *
     * @throws WebClientException in case of errors
     */
    public void resetReader() throws WebClientException {
        switch (getStatus()) {
            case WAITING_FOR_CARD -> abort();
            case DONE -> reset();
        }
    }

    /**
     * Puts the reader into payment reading mode with a zero amount.
     *
     * @throws WebClientException in case of errors
     */
    public void readBonusCard() throws WebClientException {
        // Put reader in correct state
        resetReader();

        // Initiate bogus payment
        initiatePayment(0);
    }

    /**
     * Puts the reader into payment mode.
     *
     * @param amount the amount to pay
     * @throws WebClientException in case of errors
     */
    public void readPayment(BigDecimal amount) throws WebClientException {
        // Put reader in correct state
        resetReader();

        // Initiate payment
        initiatePayment(amount.doubleValue());
    }

    /**
     * Gets the status of the card reader.
     * Direct API call.
     *
     * @return the status of the card reader
     * @throws WebClientException in case of errors
     */
    public CardReaderStatus getStatus() throws WebClientException {
        return CardReaderStatus.valueOf(makeGET("/cardreader/status", new TypeReference<>() {}));
    }

    /**
     * Gets the current payment result, if any.
     * Direct API call.
     *
     * @return the payment result
     * @throws WebClientException in case of errors
     */
    public CardReaderResult getResult() throws WebClientException {
        return makeGET("/cardreader/result", new TypeReference<>() {});
    }

    /**
     * Resets the card reader to an idle state.
     * Direct API call.
     *
     * @throws WebClientException in case of errors
     */
    public void reset() throws WebClientException {
        makePOST("/cardreader/reset", null);
    }

    /**
     * Aborts an ongoing payment.
     * Direct API call.
     *
     * @throws WebClientException in case of errors
     */
    public void abort() throws WebClientException {
        makePOST("/cardreader/abort", null);
    }

    /**
     * Instructs the card reader to accept payment.
     * Direct API call.
     *
     * @param amount the amount to pay
     * @throws WebClientException in case of errors
     */
    public void initiatePayment(double amount) throws WebClientException {
        makePOST("/cardreader/waitForPayment", Map.of("amount", String.valueOf(amount)), null);
    }

    private class WorkerThread extends Thread {
        private static final long WORKER_POLLING_INTERVAL = 1000;

        private final Consumer<CardReaderCallback> callback;

        public WorkerThread(Consumer<CardReaderCallback> callback) {
            this.callback = callback;
            setDaemon(true);
        }

        public void run() {
            try {
                // Make sure reader is in an IDLE state
                resetReader();

                CardReaderStatus previousStatus = CardReaderStatus.IDLE;
                while (!interrupted()) {
                    Thread.sleep(WORKER_POLLING_INTERVAL);

                    // Get current state and invoke callback if changed from last time
                    CardReaderStatus status = getStatus();
                    if (status != previousStatus) {
                        if (status == CardReaderStatus.DONE) {
                            callback.accept(new CardReaderCallback(status, getResult()));
                        }
                        else {
                            callback.accept(new CardReaderCallback(status, null));
                        }

                        previousStatus = status;
                    }
                }
            }
            catch (InterruptedException ignored) {
            }
            catch (WebClientException e) {
                System.out.println("Failed to poll card reader status: " + e);
            }
        }
    }
}
