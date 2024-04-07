package com.fibank.cashoperationsmodule.controller;

import com.fibank.cashoperationsmodule.model.CashBalance;
import com.fibank.cashoperationsmodule.service.CashOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class CashBalanceController {

    @Autowired
    private CashOperationService cashOperationService;

    @GetMapping("/cash-balance")
    public ResponseEntity<CashBalance> getCashBalance() {
        CashBalance balance = cashOperationService.getCurrentBalance();
        if (balance != null) {
            return ResponseEntity.ok(balance);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
