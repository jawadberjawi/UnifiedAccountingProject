//📄 OOP: Implements Interface + Encapsulation + SRP

package services;

import model.JournalEntry;
import java.util.List;

/**
 * 🔹 OOP: Implements BalanceCalculator → Polymorphism
 * 🔹 Uses private fields → Encapsulation
 * 🔹 One clear responsibility: calculate and display trial balance → SRP
 */
public class TrialBalanceCalculator implements BalanceCalculator {
    private double totalDebit;
    private double totalCredit;

    @Override
    public void calculate(List<JournalEntry> entries) {
        totalDebit = 0.0;
        totalCredit = 0.0;

        for (JournalEntry entry : entries) {
            if ("approved".equalsIgnoreCase(entry.getStatus())) {
                totalDebit += entry.getDebitTransaction().getAmount();
                totalCredit += entry.getCreditTransaction().getAmount();
            }
        }
    }

    @Override
    public void displayResult() {
        System.out.println("\n📊 Trial Balance Report:");
        System.out.println("--------------------------");
        System.out.println("Total Debit  : " + totalDebit);
        System.out.println("Total Credit : " + totalCredit);

        if (isBalanced()) {
            System.out.println("✅ Balanced. Good job!");
        } else {
            System.out.printf("❌ Not balanced! Difference: %.2f\n", getDifference());
        }
    }

    @Override
    public boolean isBalanced() {
        return totalDebit == totalCredit;
    }

    public double getDifference() {
        return Math.abs(totalDebit - totalCredit);
    }
}
