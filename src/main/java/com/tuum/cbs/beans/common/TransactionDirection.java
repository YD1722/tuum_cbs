package com.tuum.cbs.beans.common;

public enum TransactionDirection {
    IN(1),
    OUT(2);

    private int value;

    TransactionDirection(int value) {
        this.value = value;
    }

    public int getValue(){
        return this.value;
    }
}
