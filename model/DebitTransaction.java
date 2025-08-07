package model;

public class DebitTransaction extends Transaction{
    public DebitTransaction(String accountName, double amount){
        super(accountName,amount);
    }
    @Override
    public void display(){
        System.out.println("Account Name : " +getAccountName());
        System.out.println("Debit Amount : " +getAmount());
        System.out.println("----------------------------");

    }
}

