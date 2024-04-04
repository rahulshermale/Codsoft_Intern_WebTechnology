package com.example.demo;

import java.util.List;
import java.util.ArrayList;


public class User {

	private String userId;
    private String pin;
    private List<Transaction> transactionHistory;

    public User(String userId, String pin) {
        this.userId = userId;
        this.pin = pin;
        this.transactionHistory = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public String getPin() {
        return pin;
    }

    public List<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    public void addTransaction(Transaction transaction) {
        transactionHistory.add(transaction);
    }

    public double checkBalance() {
        double balance = 0.0;
        for (Transaction transaction : transactionHistory) {
            if (transaction.getType().equals("Deposit")) {
                balance += transaction.getAmount();
            } else if (transaction.getType().equals("Withdraw") || transaction.getType().startsWith("Transfer to")) {
                balance -= transaction.getAmount();
            } else if (transaction.getType().startsWith("Transfer from")) {
                balance += transaction.getAmount();
            }
        }
        return balance;
    }
}
