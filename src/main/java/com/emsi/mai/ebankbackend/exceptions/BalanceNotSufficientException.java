package com.emsi.mai.ebankbackend.exceptions;

public class BalanceNotSufficientException extends Exception  {
    public BalanceNotSufficientException(String message) {
        super(message);
    }
}
