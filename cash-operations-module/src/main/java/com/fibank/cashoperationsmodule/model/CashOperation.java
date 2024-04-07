package com.fibank.cashoperationsmodule.model;

import java.util.Map;

public class CashOperation {

    private String operationType;
    private double amount;
    private String currency;
    private Map<Integer, Integer> denominations;


    public CashOperation(String operationType, double amount, String currency, Map<Integer, Integer> denominations) {
        this.operationType = operationType;
        this.amount = amount;
        this.currency = currency;
        this.denominations = denominations;
    }

    public String getOperationType() {
        return operationType;
    }

    public double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public Map<Integer, Integer> getDenominations() {
        return denominations;
    }


}
