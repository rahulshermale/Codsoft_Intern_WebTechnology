package com.example.demo;

import java.util.HashMap;
import java.util.Map;

public class ATM {
    private Map<String, User> users; // Store users
    private User currentUser;
    private InputOutputHelper ioHelper;

    public ATM() {
        // Initialize users
        users = new HashMap<>();
        users.put("user1", new User("user1", "User01@")); // Sample user
        users.put("user2", new User("user2", "User02@")); // Sample user
        ioHelper = new InputOutputHelper();
    }

    public void start() {
        // Display login prompt and authenticate user
        login();
        // Once authenticated, display menu options and handle user input
        showMenu();
    }

    private void login() {
        ioHelper.displayMessage("Welcome to the ATM!");
        ioHelper.displayMessage("Please enter your user ID:");
        String userId = ioHelper.readInput().toLowerCase(); // Convert user ID to lowercase
        ioHelper.displayMessage("Please enter your PIN:");
        String pin = ioHelper.readInput();

        // Check if user exists and the entered PIN is correct (case-insensitive)
        if (users.containsKey(userId) && users.get(userId).getPin().equalsIgnoreCase(pin)) {
            currentUser = users.get(userId);
            ioHelper.displayMessage("Login successful. Welcome, " + userId + "!");
        } else {
            ioHelper.displayMessage("Invalid user ID or PIN. Please try again.");
            login(); // Recursive call to login again
        }
    }

    private void showMenu() {
        boolean running = true;
        while (running) {
            ioHelper.displayMessage("\nPlease select an option:");
            ioHelper.displayMessage("1. Transactions History");
            ioHelper.displayMessage("2. Withdraw");
            ioHelper.displayMessage("3. Deposit");
            ioHelper.displayMessage("4. Transfer");
            ioHelper.displayMessage("5. Check Balance");
            ioHelper.displayMessage("6. Quit");
            String choice = ioHelper.readInput();

            switch (choice) {
                case "1":
                    showTransactionHistory();
                    break;
                case "2":
                    withdraw();
                    break;
                case "3":
                    deposit();
                    break;
                case "4":
                    transfer();
                    break;
                case "5":
                    checkBalance(currentUser.getUserId());
                    break;
                case "6":
                    running = false;
                    break;
                default:
                    ioHelper.displayMessage("Invalid option. Please try again.");
            }
        }
    }

    private void showTransactionHistory() {
        ioHelper.displayMessage("\nTransaction History:");
        for (Transaction transaction : currentUser.getTransactionHistory()) {
            String transactionType = transaction.getType();
            String userIdInfo = "";
            if (transactionType.equals("Deposit")) {
                userIdInfo = "To Account of user Id: " + currentUser.getUserId();
            } else if (transactionType.equals("Withdraw")) {
                userIdInfo = "From Account of user Id: " + currentUser.getUserId();
            } else if (transactionType.startsWith("Transfer to")) {
                String recipientId = transactionType.substring(12); // Extract recipient user ID
                userIdInfo = "To Account of user Id: " + recipientId;
            } else if (transactionType.startsWith("Transfer from")) {
                String senderId = transactionType.substring(14); // Extract sender user ID
                userIdInfo = "From Account of user Id: " + senderId;
            }
            ioHelper.displayMessage("Type: " + transactionType + ", " + userIdInfo +
                    ", Amount: " + transaction.getAmount() + ", Date: " + transaction.getDate());
        }
    }

    private double checkBalance(String userId) {
        double balance = 0.0;
        User user = users.get(userId);
        if (user != null) {
            for (Transaction transaction : user.getTransactionHistory()) {
                if (transaction.getType().equals("Deposit")) {
                    balance += transaction.getAmount();
                } else if (transaction.getType().equals("Withdraw") || transaction.getType().startsWith("Transfer to")) {
                    balance -= transaction.getAmount();
                } else if (transaction.getType().startsWith("Transfer from")) {
                    balance += transaction.getAmount();
                }
            }
            ioHelper.displayMessage("Current Balance for " + userId + ": Rs" + balance);
        } else {
            ioHelper.displayMessage("User not found.");
        }
        return balance;
    }

    private void withdraw() {
        ioHelper.displayMessage("\nEnter amount to withdraw:");
        double amount = Double.parseDouble(ioHelper.readInput());
        double balance = checkBalance(currentUser.getUserId()); // Get current balance
        if (balance >= amount) {
            if (amount > 0) {
                currentUser.addTransaction(new Transaction("Withdraw", amount));
                ioHelper.displayMessage("Withdrawal of Rs" + amount + " successful.");
            } else {
                ioHelper.displayMessage("Invalid amount. Please enter a positive number.");
            }
        } else {
            ioHelper.displayMessage("Insufficient balance.");
        }
    }

    private void deposit() {
        ioHelper.displayMessage("\nEnter amount to deposit:");
        double amount = Double.parseDouble(ioHelper.readInput());
        if (amount > 0) {
            currentUser.addTransaction(new Transaction("Deposit", amount));
            ioHelper.displayMessage("Deposit of Rs" + amount + " successful.");
        } else {
            ioHelper.displayMessage("Invalid amount. Please enter a positive number.");
        }
    }
 
    private void transfer() {
        ioHelper.displayMessage("\nEnter recipient's user ID:");
        String recipientId = ioHelper.readInput();
        
        // Check if recipient ID is the same as sender's ID
        if (recipientId.equals(currentUser.getUserId())) {
            ioHelper.displayMessage("Cannot transfer money to the same account.");
            return;
        }
        
        if (!users.containsKey(recipientId)) {
            ioHelper.displayMessage("Recipient user ID not found.");
            return;
        }
        
        ioHelper.displayMessage("Enter amount to transfer:");
        double amount = Double.parseDouble(ioHelper.readInput());
        double senderBalance = currentUser.checkBalance(); // Get sender's current balance
        if (amount <= 0 || senderBalance < amount) {
            ioHelper.displayMessage("Invalid amount. Please enter a positive number and ensure sufficient balance.");
            return;
        }
        
        // Deduct amount from sender's account
        currentUser.addTransaction(new Transaction("Transfer to " + recipientId, +amount));
        
        // Add amount to recipient's account
        User recipient = users.get(recipientId);
        recipient.addTransaction(new Transaction("Transfer from " + currentUser.getUserId(), amount));
        
        ioHelper.displayMessage("Transfer of Rs" + amount + " to " + recipientId + " successful.");
        
        // Update balance after transfer for both users
        checkBalance("user1");
        checkBalance("user2");
    }

}
