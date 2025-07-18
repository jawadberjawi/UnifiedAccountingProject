//📄 OOP Concept: Abstraction

package services;

import model.JournalEntry;
import java.util.List;

/**
 * 🔹 OOP: Interface → Abstraction
 * This interface defines a "contract" for all balance calculators.
 * Any class that implements this must provide these 3 methods.
 */
public interface BalanceCalculator {
    void calculate(List<JournalEntry> entries);   // Calculate totals
    void displayResult();                         // Print result to user
    boolean isBalanced();                         // Check if debit == credit
}
