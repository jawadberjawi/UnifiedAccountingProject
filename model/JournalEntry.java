package model;

import java.time.LocalDate;

// 🔹 Represents one full journal entry (debit + credit)
public class JournalEntry {

    // 🔐 Fields (Encapsulation)
    private String transactionID;
    private LocalDate date;
    private String createdBy;
    private String status;
    private DebitTransaction debitTransaction;
    private CreditTransaction creditTransaction;

    // ✅ Constructor sets all values
    public JournalEntry(String transactionID, LocalDate date,
                        DebitTransaction debitTransaction,
                        CreditTransaction creditTransaction,
                        String createdBy, String status) {
        this.transactionID = transactionID;
        this.date = date;
        this.createdBy = createdBy;
        this.status = status;
        this.debitTransaction = debitTransaction;
        this.creditTransaction = creditTransaction;
    }

    // 📄 Shows the full journal entry details
    public void display() {
        System.out.println("Transaction ID : " + transactionID);
        System.out.println("Date           : " + date);
        System.out.println("Created By     : " + createdBy);
        System.out.println("Status         : " + status);
        System.out.println("----- Debit Entry -----");
        debitTransaction.display();
        System.out.println("----- Credit Entry -----");
        creditTransaction.display();
        System.out.println("===============================");
    }

    // 🧮 Returns the total amount (debit + credit)
    public double getAmount() {
        return debitTransaction.getAmount(); // same as credit
    }

    // 🔍 Getters (to access private fields)
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

    // 🔎 Optional: Return one account name (for display/filtering)
    public String getAccountName() {
        if (debitTransaction != null) {
            return debitTransaction.getAccountName();
        } else if (creditTransaction != null) {
            return creditTransaction.getAccountName();
        } else {
            return "Unknown";
        }
    }
}
