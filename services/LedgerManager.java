package services;

import model.JournalEntry;
import model.DebitTransaction;
import model.CreditTransaction;

import java.util.*;

public class LedgerManager {

    private static final int WIDTH = 9;
    private final Map<String, List<JournalEntry>> ledgerMap = new HashMap<>();

    public void groupEntries(List<JournalEntry> allEntries) {
        for (JournalEntry entry : allEntries) {
            DebitTransaction debit = entry.getDebitTransaction();
            if (debit != null) {
                ledgerMap
                        .computeIfAbsent(debit.getAccountName(), k -> new ArrayList<>())
                        .add(entry);
            }

            CreditTransaction credit = entry.getCreditTransaction();
            if (credit != null) {
                ledgerMap
                        .computeIfAbsent(credit.getAccountName(), k -> new ArrayList<>())
                        .add(entry);
            }
        }
    }

    public void displayLedger() {
        System.out.println("\n📒 Ledger View:");
        System.out.println("----------------------------");

        for (Map.Entry<String, List<JournalEntry>> accountEntry : ledgerMap.entrySet()) {
            String account = accountEntry.getKey();
            List<JournalEntry> entries = accountEntry.getValue();

            System.out.println("\nAccount: " + account);
            System.out.println("Date       | Debit     | Credit");
            System.out.println("-------------------------------");

            for (JournalEntry entry : entries) {
                String date = entry.getDate().toString();
                String debitAmount = "";
                String creditAmount = "";

                if (entry.getDebitTransaction() != null &&
                        account.equals(entry.getDebitTransaction().getAccountName())) {
                    debitAmount = String.valueOf(entry.getDebitTransaction().getAmount());
                }

                if (entry.getCreditTransaction() != null &&
                        account.equals(entry.getCreditTransaction().getAccountName())) {
                    creditAmount = String.valueOf(entry.getCreditTransaction().getAmount());
                }

                System.out.println(date + " | " + formatAmount(debitAmount) + " | " + formatAmount(creditAmount));
            }
        }
    }

    private String formatAmount(String amount) {
        int padding = Math.max(0, WIDTH - amount.length());
        return " ".repeat(padding) + amount;
    }

    public void removeAccount(String accountName) {
        if (ledgerMap.remove(accountName) != null) {
            System.out.println("Removed account: " + accountName);
        } else {
            System.out.println("Account not found: " + accountName);
        }
    }

    public Set<String> getAllAccountNames() {
        return new HashSet<>(ledgerMap.keySet());
    }
}