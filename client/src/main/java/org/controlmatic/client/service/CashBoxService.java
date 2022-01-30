package org.controlmatic.client.service;

import com.fasterxml.jackson.core.type.TypeReference;
import org.controlmatic.client.model.CashBoxStatus;
import org.controlmatic.shared.domain.WebClientException;
import org.controlmatic.shared.util.WebClient;

import java.util.function.Consumer;

/**
 * Class for interacting with an external Cash Box.
 */
public class CashBoxService extends WebClient {
    /**
     * Create a new cash box service.
     *
     * @param endpoint the url to the cash box, e.g. http://localhost:9001
     */
    public CashBoxService(String endpoint) {
        super(endpoint);
    }

    /**
     * Create a new cash box service with an automatic poller.
     *
     * @param endpoint endpoint the url to the cash box, e.g. http://localhost:9001
     * @param callback a callback for box events
     */
    public CashBoxService(String endpoint, Consumer<CashBoxStatus> callback) {
        this(endpoint);

        new WorkerThread(callback).start();
    }

    /**
     * Gets the status of the cash box.
     *
     * @return the status of the cash box
     * @throws WebClientException in case of errors
     */
    public CashBoxStatus getStatus() throws WebClientException {
        return CashBoxStatus.valueOf(makeGET("/cashbox/status", new TypeReference<>() {}));
    }

    /**
     * Opens the cash box.
     *
     * @throws WebClientException in case of errors
     */
    public void open() throws WebClientException {
        makePOST("/cashbox/open", null);
    }

    private class WorkerThread extends Thread {
        private static final long WORKER_POLLING_INTERVAL = 2000;

        private final Consumer<CashBoxStatus> callback;

        public WorkerThread(Consumer<CashBoxStatus> callback) {
            this.callback = callback;
            setDaemon(true);
        }

        public void run() {
            try {
                CashBoxStatus previousStatus = CashBoxStatus.CLOSED;
                while (!interrupted()) {
                    Thread.sleep(WORKER_POLLING_INTERVAL);

                    // Get current state and invoke callback if changed from last time
                    CashBoxStatus status = getStatus();
                    if (status != previousStatus) {
                        callback.accept(status);
                        previousStatus = status;
                    }
                }
            }
            catch (InterruptedException ignored) {
            }
            catch (WebClientException e) {
                System.out.println("Failed to poll cash box status: " + e);
            }
        }
    }
}
