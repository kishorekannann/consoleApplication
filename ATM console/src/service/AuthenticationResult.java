package service;

import model.Account;

public class AuthenticationResult {
    private final boolean success;
    private final String message;
    private final Account account;

    public AuthenticationResult(boolean success, String message, Account account) {
        this.success = success;
        this.message = message;
        this.account = account;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Account getAccount() {
        return account;
    }
}