package com.fibank.cashoperationsmodule.service;

import com.fibank.cashoperationsmodule.model.CashBalance;
import com.fibank.cashoperationsmodule.model.CashOperation;
import com.fibank.cashoperationsmodule.model.CashOperationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CashOperationService {

    private double bgnBalance = 1000;
    private double eurBalance = 2000;


    @Value("${app.balances.file-path}")
    private String balanceFilePath;
    private Map<Integer, Integer> bgnDenominations = new HashMap<>();
    private Map<Integer, Integer> eurDenominations = new HashMap<>();

    private final FileService fileService;

    public CashOperationService(FileService fileService) {
        this.fileService = fileService;
        bgnDenominations.put(50, 10);
        bgnDenominations.put(10, 50);
        eurDenominations.put(100, 10);
        eurDenominations.put(50, 20);
    }


    public synchronized CashOperationResponse processCashOperation(CashOperation request) {
        String operationType = request.getOperationType().toLowerCase();
        double amount = request.getAmount();
        String currency = request.getCurrency().toUpperCase();
        Map<Integer, Integer> denominations = request.getDenominations();

        CashOperationResponse response;
        switch (operationType) {
            case "deposit":
                response = processDeposit(amount, denominations, getDenominationsMap(currency), currency);
                break;
            case "withdrawal":
                response = processWithdrawal(amount, denominations, getDenominationsMap(currency), currency);
                break;
            default:
                return new CashOperationResponse(false, "Invalid operation type");
        }

        if (response.isSuccess()) {
            String transactionLog = String.format("%s %s: %f %s",
                    operationType.toUpperCase(), currency, amount,
                    denominationsToString(denominations));
            fileService.appendTransaction(transactionLog);

            updateBalanceFile();
        }

        return response;
    }

    private void updateBalanceFile() {
        String balanceInfo = String.format("BGN Balance: %.2f, Denominations: %s\nEUR Balance: %.2f, Denominations: %s",
                bgnBalance, denominationsToString(bgnDenominations),
                eurBalance, denominationsToString(eurDenominations));
        fileService.updateBalances(balanceInfo);
    }

    private String denominationsToString(Map<Integer, Integer> denominations) {
        return denominations.entrySet().stream()
                .map(e -> e.getKey() + "x" + e.getValue())
                .collect(Collectors.joining(", "));
    }


    private Map<Integer, Integer> getDenominationsMap(String currency) {
        return "BGN".equals(currency) ? bgnDenominations : eurDenominations;
    }

    private double getBalance(String currency) {
        return "BGN".equals(currency) ? bgnBalance : eurBalance;
    }

    private void setBalance(String currency, double amount) {
        if ("BGN".equals(currency)) {
            bgnBalance = amount;
        } else if ("EUR".equals(currency)) {
            eurBalance = amount;
        }
    }

    private CashOperationResponse processDeposit(double amount, Map<Integer, Integer> denominations,
                                                 Map<Integer, Integer> currencyDenominations, String currency) {
        denominations.forEach((denomination, count) ->
                currencyDenominations.merge(denomination, count, Integer::sum));
        setBalance(currency, getBalance(currency) + amount);

        return new CashOperationResponse(true, "Deposit processed successfully for " + currency);
    }



    private CashOperationResponse processWithdrawal(double amount, Map<Integer, Integer> requestDenominations,
                                                    Map<Integer, Integer> currencyDenominations, String currency) {
        for (Map.Entry<Integer, Integer> entry : requestDenominations.entrySet()) {
            int denomination = entry.getKey();
            int requiredCount = entry.getValue();
            int availableCount = currencyDenominations.getOrDefault(denomination, 0);

            if (availableCount < requiredCount) {
                return new CashOperationResponse(false, "Insufficient denominations for withdrawal");
            }

            currencyDenominations.put(denomination, availableCount - requiredCount);
        }

        if (getBalance(currency) < amount) {
            return new CashOperationResponse(false, "Insufficient balance for withdrawal");
        }
        setBalance(currency, getBalance(currency) - amount);

        return new CashOperationResponse(true, "Withdrawal processed successfully for " + currency);
    }


    public CashBalance getCurrentBalanceFromFile() {



        String filePath = balanceFilePath;
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            double bgnBalance = Double.parseDouble(lines.get(0).split(",")[0].split(":")[1].trim());
            Map<Integer, Integer> bgnDenominations = parseDenominations(lines.get(0).split(":")[2].trim());
            double eurBalance = Double.parseDouble(lines.get(1).split(",")[0].split(":")[1].trim());
            Map<Integer, Integer> eurDenominations = parseDenominations(lines.get(1).split(":")[2].trim());

            return new CashBalance(bgnBalance, eurBalance, bgnDenominations, eurDenominations);
        } catch (IOException e) {
            e.printStackTrace();
            return getDefaultBalance();
        }

    }

    private CashBalance getDefaultBalance() {
        Map<Integer, Integer> defaultBgnDenominations = new HashMap<>();
        Map<Integer, Integer> defaultEurDenominations = new HashMap<>();
        return new CashBalance(0, 0, defaultBgnDenominations, defaultEurDenominations);
    }

    private Map<Integer, Integer> parseDenominations(String denominationsStr) {
        Map<Integer, Integer> denominations = new HashMap<>();
        String[] parts = denominationsStr.split(",");
        for (String part : parts) {
            String[] denomParts = part.trim().split("x");
            int quantity = Integer.parseInt(denomParts[0]);
            int denomination = Integer.parseInt(denomParts[1]);
            denominations.put(denomination, quantity);
        }
        return denominations;
    }

    public CashBalance getCurrentBalance() {
            return getCurrentBalanceFromFile();
    }
}
