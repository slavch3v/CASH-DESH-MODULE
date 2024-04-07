package com.fibank.cashoperationsmodule.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Service
public class FileService {

    @Value("${app.transactions.file-path}")
    private String transactionFilePath;

    @Value("${app.balances.file-path}")
    private String balanceFilePath;


    public void appendTransaction(String content) {
        appendToFile(transactionFilePath, content + System.lineSeparator());
    }

    public void updateBalances(String content) {
        writeToNewFile(balanceFilePath, content);
    }

    private void appendToFile(String filePath, String content) {
        try {
            Path path = Paths.get(filePath).toAbsolutePath();
            Files.write(path, content.getBytes(), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
        } catch (IOException e) {
            System.err.println("Error appending to file: " + e.getMessage());
        }
    }

    private void writeToNewFile(String filePath, String content) {
        try {
            Path path = Paths.get(filePath).toAbsolutePath();
            Files.write(path, content.getBytes(), StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
        } catch (IOException e) {
            System.err.println("Error writing to new file: " + e.getMessage());
        }
    }
}
