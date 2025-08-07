package services;

import model.JournalEntry;
import java.util.List;

/**
 * TrialBalanceCalculator implements BalanceCalculator interface.
 * It calculates and displays total debit and credit for approved entries,
 * and checks whether the journal is balanced.
 */
public class TrialBalanceCalculator implements BalanceCalculator {
    private double debitTotal;
    private double creditTotal;

    // 🔢 Calculate total debit and credit for approved entries
    @Override
    public void calculate(List<JournalEntry> entries) {
        debitTotal = 0.0;
        creditTotal = 0.0;

        for (JournalEntry entry : entries) {
            if ("approved".equalsIgnoreCase(entry.getStatus())) {
                debitTotal += entry.getDebitTransaction().getAmount();
                creditTotal += entry.getCreditTransaction().getAmount();
            }
        }
    }

    // 📊 Display the results of the trial balance
    @Override
    public void displayResult() {
        System.out.printf("Total Debit  : %.2f%n", debitTotal);
        System.out.printf("Total Credit : %.2f%n", creditTotal);

        if (isBalanced()) {
            System.out.println("✅ Balanced.");
        } else {
            System.out.printf("❌ Not balanced. Difference: %.2f%n", getDifference());
        }
    }

    // ✅ Check if totals are equal
    @Override
    public boolean isBalanced() {
        return debitTotal == creditTotal;
    }

    // 🔁 Calculate the difference between totals
    public double getDifference() {
        return Math.abs(debitTotal - creditTotal);
    }
}
