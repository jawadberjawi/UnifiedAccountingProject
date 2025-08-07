package services;

import java.util.List;
import model.JournalEntry;

/**
 * 🔹 OOP: Uses interface reference → Polymorphism
 * 🔹 Constructor receives dependency → Dependency Injection
 * 🔹 This class only prints → SRP (Single Responsibility Principle)
 * 🔹 Supports any calculator → Open/Closed Principle
 */
public class ReportPrinter {

    private BalanceCalculator calculator;

    // ✅ Constructor with dependency injection
    public ReportPrinter(BalanceCalculator calculator) {
        this.calculator = calculator;
    }

    // ✅ Method to trigger the report logic
    public void printReport(List<JournalEntry> entries) {
        calculator.calculate(entries);
        calculator.displayResult();
    }
}
