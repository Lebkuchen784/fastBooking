package com.project.bookingService.user.businessOwner;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum PaymentMethod {
    @JsonProperty("PAYPAL")
    PAYPAL("Paypal"),
    @JsonProperty("DEBIT_CARD")
    DEBIT_CARD("Debit card"),
    @JsonProperty("CREDIT_CARD")
    CREDIT_CARD("Credit card"),
    @JsonProperty("BITCOIN")
    BITCOIN("Bitcoin"),
    @JsonProperty("SEPA_BANK_TRANSFER")
    SEPA_BANK_TRANSFER("SEPA Bank Transfer");

    private final String label;

    PaymentMethod(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
