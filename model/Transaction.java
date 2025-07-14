package model;

public abstract class Transaction { // 🔹 Abstraction (can't be used directly)
    private String accountName;      // 🔹 Encapsulation (private fields)
    private double amount;

    public Transaction(String accountName, double amount) {
        this.accountName = accountName;
        this.amount = amount;
    }

    public String getAccountName() {
        return accountName; // 🔹 Encapsulation (controlled access)
    }

    public double getAmount() {
        return amount;
    }

    public abstract void display(); // 🔹 Abstraction (forced on children)
}
