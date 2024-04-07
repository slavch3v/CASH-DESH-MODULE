package com.fibank.cashoperationsmodule.model;

public class CashOperationResponse {
    private boolean success;
    private String message;


    public CashOperationResponse() {
    }

    public CashOperationResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
