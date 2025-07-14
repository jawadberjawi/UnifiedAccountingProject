package model;

public class DebitTransaction extends Transaction { // 🔹 Inheritance

    public DebitTransaction(String accountName, double amount) {
        super(accountName, amount);
    }

    @Override
    public void display() { // 🔹 Polymorphism (overrides display)
        System.out.println("💸 Debit Transaction");
        System.out.println("Account: " + getAccountName());
        System.out.println("Amount: " + getAmount());
        System.out.println("--------------------------------");
    }
}
