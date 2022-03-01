package com.tuum.cbs.beans.common.response;

public class TransactionResponse {
    private String accountId;
    private int transactionId; // TODO: How to get transaction id
    private double amount;
    private String currencyCode;
    private int transactionDirection;
    private String description;
    private double newBalance;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public int getTransactionDirection() {
        return transactionDirection;
    }

    public void setTransactionDirection(int transactionDirection) {
        this.transactionDirection = transactionDirection;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getNewBalance() {
        return newBalance;
    }

    public void setNewBalance(double newBalance) {
        this.newBalance = newBalance;
    }
}
