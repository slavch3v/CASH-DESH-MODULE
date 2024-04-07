package com.fibank.cashoperationsmodule.controller;

import com.fibank.cashoperationsmodule.model.CashOperation;
import com.fibank.cashoperationsmodule.model.CashOperationResponse;
import com.fibank.cashoperationsmodule.service.CashOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class CashOperationController {

    @Autowired
    private CashOperationService cashOperationService;

    @PostMapping("/cash-operation")
    public ResponseEntity<String> handleCashOperation(@RequestBody CashOperation operation) {
        CashOperationResponse response = cashOperationService.processCashOperation(operation);
        if (response.isSuccess()) {
            return ResponseEntity.ok("Operation processed successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to process operation");
        }
    }
}
