package model;

public class CreditTransaction extends Transaction { // 🔹 Inheritance

    public CreditTransaction(String accountName, double amount) {
        super(accountName, amount);
    }

    @Override
    public void display() { // 🔹 Polymorphism (overrides display)
        System.out.println("💰 Credit Transaction");
        System.out.println("Account: " + getAccountName());
        System.out.println("Amount: " + getAmount());
        System.out.println("--------------------------------");
    }
}

