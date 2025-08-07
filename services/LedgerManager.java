package services;

import model.JournalEntry;
import java.util.*;

/**
 * 📘 LedgerManager.java
 *
 * This class organizes journal entries by account and displays them
 * in a Ledger format.
 *
 * 🔹 Key Concepts: HashMap, List grouping, formatting, clean display
 */
public class LedgerManager {

    private static final int WIDTH = 9;

    // 🔹 Groups entries by account name
    private final Map<String, List<JournalEntry>> ledgerMap = new HashMap<>();

    /**
     * Step 1: Group all journal entries by account name (debit & credit separately)
     */
    public void groupEntries(List<JournalEntry> allEntries) {
        for (JournalEntry entry : allEntries) {
            // Normalize account names to lowercase to avoid duplication due to case
            String debitAccount = entry.getDebitTransaction().getAccountName().toLowerCase();
            String creditAccount = entry.getCreditTransaction().getAccountName().toLowerCase();

            // Add entry under debit account
            ledgerMap.computeIfAbsent(debitAccount, k -> new ArrayList<>()).add(entry);

            // Add entry under credit account only if it's not the same
            if (!creditAccount.equals(debitAccount)) {
                ledgerMap.computeIfAbsent(creditAccount, k -> new ArrayList<>()).add(entry);
            }
        }
    }

    /**
     * Step 2: Display the Ledger view for all accounts
     */
    public void displayLedger() {
        System.out.println("\n📒 Ledger View:");
        System.out.println("----------------------------");

        for (Map.Entry<String, List<JournalEntry>> accountEntry : ledgerMap.entrySet()) {
            String account = accountEntry.getKey();
            List<JournalEntry> entries = accountEntry.getValue();

            System.out.println("\nAccount: " + capitalize(account));
            System.out.println("Date       | Debit     | Credit");
            System.out.println("-------------------------------");

            for (JournalEntry entry : entries) {
                String date = entry.getDate().toString();

                String debit = entry.getDebitTransaction().getAccountName().equalsIgnoreCase(account)
                        ? String.valueOf(entry.getDebitTransaction().getAmount()) : "";

                String credit = entry.getCreditTransaction().getAccountName().equalsIgnoreCase(account)
                        ? String.valueOf(entry.getCreditTransaction().getAmount()) : "";

                System.out.println(date + " | " + formatAmount(debit) + " | " + formatAmount(credit));
            }
        }
    }

    /**
     * Step 3: Aligns debit/credit values using fixed width
     */
    private String formatAmount(String amount) {
        return " ".repeat(Math.max(0, WIDTH - amount.length())) + amount;
    }

    /**
     * Optional: Capitalize first letter of account for clean display
     */
    private String capitalize(String account) {
        if (account == null || account.isEmpty()) return account;
        return account.substring(0, 1).toUpperCase() + account.substring(1);
    }

    /**
     * Step 4 (Optional): Remove an account and its entries
     */
    public void removeAccount(String accountName) {
        String normalized = accountName.toLowerCase();
        if (ledgerMap.containsKey(normalized)) {
            ledgerMap.remove(normalized);
            System.out.println("Removed account: " + capitalize(normalized));
        } else {
            System.out.println("Account not found: " + capitalize(normalized));
        }
    }

    /**
     * Step 5 (Optional): Return a set of all account names
     */
    public Set<String> getAllAccountNames() {
        return new HashSet<>(ledgerMap.keySet());
    }
}
