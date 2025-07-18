//📄 OOP: Dependency Injection + SRP + Open/Closed Principle

package services;
import model.JournalEntry;
import java.util.List;

/**
 * 🔹 OOP: Uses interface reference → Polymorphism
 * 🔹 Constructor receives dependency → Dependency Injection
 * 🔹 This class only prints → SRP (Single Responsibility Principle)
 * 🔹 Supports any calculator → Open/Closed Principle
 */
public class ReportPrinter {
    private BalanceCalculator calculator;

    public ReportPrinter(BalanceCalculator calculator) {
        this.calculator = calculator;
    }

    public void printReport(List<JournalEntry> entries) {
        calculator.calculate(entries);
        calculator.displayResult();
    }
}
