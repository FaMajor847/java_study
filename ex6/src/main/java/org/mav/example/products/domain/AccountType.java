package org.mav.example.products.domain;

public enum AccountType {
    ACCOUNT("ACCOUNT"),
    CARD("CARD");

    private final String value;

    AccountType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
