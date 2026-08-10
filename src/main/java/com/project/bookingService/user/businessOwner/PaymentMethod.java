package com.project.bookingService.user.businessOwner;

public enum PaymentMethod {
    PAYPAL("Paypal"),
    DEBIT_CARD("Debit card"),
    CREDIT_CARD("Credit card"),
    BITCOIN("Bitcoin"),
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
