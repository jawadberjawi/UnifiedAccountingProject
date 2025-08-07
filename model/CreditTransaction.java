package model;

public class CreditTransaction extends Transaction {

    public CreditTransaction(String accountName, double amount) {
        super(accountName, amount);
    }

    @Override
    public void display() {
        System.out.println("💰 Credit Transaction");
        System.out.println("Credit Account: " + getAccountName());
        System.out.println("Credit Amount : " + getAmount());
        System.out.println("--------------------------------");
    }
}
