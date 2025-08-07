package model;

// 🔹 Inherits from abstract Transaction class
public class CreditTransaction extends Transaction {

    // 🔹 Passes parameters to the parent class constructor
    public CreditTransaction(String accountName, double amount) {
        super(accountName, amount);
    }

    // 🔹 Overrides the abstract display method with credit-specific info
    @Override
    public void display() {
        System.out.println("💰 Credit Transaction");
        System.out.println("Credit Account: " + getAccountName());
        System.out.println("Credit Amount : " + getAmount());
        System.out.println("--------------------------------");
    }
}
