package com.fibank.cashoperationsmodule.model;

import java.util.Map;

public class CashBalance {
    private double bgnBalance;
    private double eurBalance;
    private Map<Integer, Integer> bgnDenominations;
    private Map<Integer, Integer> eurDenominations;

    public CashBalance(double bgnBalance, double eurBalance, Map<Integer, Integer> bgnDenominations,
                       Map<Integer, Integer> eurDenominations) {
        this.bgnBalance = bgnBalance;
        this.eurBalance = eurBalance;
        this.bgnDenominations = bgnDenominations;
        this.eurDenominations = eurDenominations;
    }
    public Map<Integer, Integer> getBgnDenominations() {
        return bgnDenominations;
    }

    public Map<Integer, Integer> getEurDenominations() {
        return eurDenominations;
    }


}
