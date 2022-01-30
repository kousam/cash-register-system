package org.controlmatic.shared.request;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

/**
 * Request object for querying stored sales.
 * Create a new object and use setters to specify query constraints.
 */
public class SalesQueryRequest {
    private LocalDateTime dateTimeStart;
    private LocalDateTime dateTimeEnd;
    private int customerAgeStart;
    private int customerAgeEnd;
    private String customerSex;

    /**
     * Default constructor, use setters to modify the request.
     */
    public SalesQueryRequest() {
        this.dateTimeStart = LocalDateTime.MAX;
        this.dateTimeEnd = LocalDateTime.MIN;
        this.customerAgeStart = 0;
        this.customerAgeEnd = 0;
        this.customerSex = null;
    }

    public LocalDateTime getDateTimeStart() {
        return dateTimeStart;
    }

    public void setDateTimeStart(LocalDateTime dateTimeStart) {
        this.dateTimeStart = dateTimeStart;
    }

    public LocalDateTime getDateTimeEnd() {
        return dateTimeEnd;
    }

    public void setDateTimeEnd(LocalDateTime dateTimeEnd) {
        this.dateTimeEnd = dateTimeEnd;
    }

    public int getCustomerAgeStart() {
        return customerAgeStart;
    }

    public void setCustomerAgeStart(int customerAgeStart) {
        this.customerAgeStart = customerAgeStart;
    }

    public int getCustomerAgeEnd() {
        return customerAgeEnd;
    }

    public void setCustomerAgeEnd(int customerAgeEnd) {
        this.customerAgeEnd = customerAgeEnd;
    }

    public String getCustomerSex() {
        return customerSex;
    }

    public void setCustomerSex(String customerSex) {
        this.customerSex = customerSex;
    }

    @JsonIgnore
    public boolean hasCustomerRequirements() {
        return customerAgeStart != 0 || customerAgeEnd != 0 || customerSex != null;
    }
}
