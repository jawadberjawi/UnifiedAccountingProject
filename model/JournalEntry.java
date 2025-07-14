package model;

import java.time.LocalDate;

public class JournalEntry {

    // 🔐 Fields (Encapsulation)
    private String transactionID;
    private LocalDate date;
    private String createdBy;
    private String status;

    private DebitTransaction debitTransaction;
    private CreditTransaction creditTransaction;

    // ✅ Constructor to initialize all fields
    public JournalEntry(String transactionID, LocalDate date,
                        DebitTransaction debitTransaction, CreditTransaction creditTransaction,
                        String createdBy, String status) {
        this.transactionID = transactionID;
        this.date = date;
        this.debitTransaction = debitTransaction;
        this.creditTransaction = creditTransaction;
        this.createdBy = createdBy;
        this.status = status;
    }

    // 📄 Display full entry details
    public void display() {
        System.out.println("📄 Transaction ID: " + transactionID);
        System.out.println("📅 Date: " + date);
        System.out.println("👤 Created By: " + createdBy);
        System.out.println("✅ Status: " + status);

        System.out.println("----- Debit Entry -----");
        debitTransaction.display();

        System.out.println("----- Credit Entry -----");
        creditTransaction.display();

        System.out.println("====================================");
    }

    // 🧮 Get total amount from debit and credit combined
    public double getAmount() {
        return debitTransaction.getAmount() + creditTransaction.getAmount();
    }

    // 🔍 Getters (Encapsulation - read access only)
    public String getTransactionID() {
        return transactionID;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getStatus() {
        return status;
    }

    public DebitTransaction getDebitTransaction() {
        return debitTransaction;
    }

    public CreditTransaction getCreditTransaction() {
        return creditTransaction;
    }
}
