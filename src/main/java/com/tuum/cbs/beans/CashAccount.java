package com.tuum.cbs.beans;

public class CashAccount {
    private int cashAccountId;
    private String bankAccountId;
    private String currencyCode;
    private double balance; // TODO: Determine data type
    private double availableBalance;

    public int getCashAccountId() {
        return cashAccountId;
    }

    public void setCashAccountId(int cashAccountId) {
        this.cashAccountId = cashAccountId;
    }

    public String getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(String bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(double availableBalance) {
        this.availableBalance = availableBalance;
    }
}
