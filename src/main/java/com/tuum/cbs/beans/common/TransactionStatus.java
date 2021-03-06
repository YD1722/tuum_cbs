package com.tuum.cbs.beans.common;

public enum TransactionStatus {
    SUCCESS(1),
    ERROR(2);

    private int value;

    TransactionStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
