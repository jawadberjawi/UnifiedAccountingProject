package model;

// 🔹 Abstract base class for all types of transactions (like Debit or Credit)
public abstract class Transaction {

    // 🔹 Private fields (Encapsulation)
    private String accountName;
    private double amount;

    // 🔹 Constructor to set account name and amount when creating a transaction
    public Transaction(String accountName, double amount) {
        this.accountName = accountName;
        this.amount = amount;
    }

    // 🔹 Getter methods to access private fields
    public String getAccountName() {
        return accountName;
    }

    public double getAmount() {
        return amount;
    }

    // 🔹 Abstract method to be implemented by child classes
    public abstract void display();

}

