package main;

import model.*;
import manager.UnifiedJournalManager;
import services.BalanceCalculator;
import services.ReportPrinter;
import services.TrialBalanceCalculator;
import utils.InputValidator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ArrayList<JournalEntry> entries = new ArrayList<>();
        UnifiedJournalManager manager = new UnifiedJournalManager(entries);
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n📋 Journal Entry System");
            System.out.println("1. ➕ Add Entry");
            System.out.println("2. 📘 Display All Entries");
            System.out.println("3. 🔍 Filter by Status + Creator + Min Amount");
            System.out.println("4. 🧾 Generate Trial Balance Report");
            System.out.println("0. ❌ Exit");

            System.out.print("👉 Choose an option: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    System.out.print("🆔 Enter Transaction ID: ");
                    String transactionID = scanner.nextLine();

                    LocalDate date;
                    while (true) {
                        System.out.print("📅 Enter Date (YYYY-MM-DD): ");
                        try {
                            date = LocalDate.parse(scanner.nextLine());
                            if (InputValidator.isValidDate(date)) break;
                            System.out.println("❌ Date cannot be in the future.");
                        } catch (Exception e) {
                            System.out.println("❌ Invalid format. Try again.");
                        }
                    }

                    String debitAccount;
                    while (true) {
                        System.out.print("🏦 Enter Debit Account: ");
                        debitAccount = scanner.nextLine();
                        if (InputValidator.isValidAccount(debitAccount)) break;
                        System.out.println("❌ Invalid account name.");
                    }

                    String creditAccount;
                    while (true) {
                        System.out.print("🏦 Enter Credit Account: ");
                        creditAccount = scanner.nextLine();
                        if (InputValidator.isValidAccount(creditAccount)) break;
                        System.out.println("❌ Invalid account name.");
                    }

                    double amount;
                    while (true) {
                        System.out.print("💰 Enter Transaction Amount: ");
                        try {
                            amount = Double.parseDouble(scanner.nextLine());
                            if (InputValidator.isValidAmount(amount)) break;
                            System.out.println("❌ Amount must be > 0.");
                        } catch (Exception e) {
                            System.out.println("❌ Invalid amount format.");
                        }
                    }

                    String createdBy;
                    while (true) {
                        System.out.print("👤 Enter Created By: ");
                        createdBy = scanner.nextLine();
                        if (InputValidator.isValidAccount(createdBy)) break;
                        System.out.println("❌ Invalid name.");
                    }

                    String status;
                    while (true) {
                        System.out.print("✅ Enter Status (approved/pending): ");
                        status = scanner.nextLine().trim();
                        if (InputValidator.isValidStatus(status)) break;
                        System.out.println("❌ Must be 'approved' or 'pending'.");
                    }

                    DebitTransaction debit = new DebitTransaction(debitAccount, amount);
                    CreditTransaction credit = new CreditTransaction(creditAccount, amount);
                    JournalEntry entry = new JournalEntry(transactionID, date, debit, credit, createdBy, status);

                    manager.addEntry(entry);
                    System.out.println("✅ Journal Entry Added!");
                    break;

                case 2:
                    manager.displayAllEntries();
                    break;

                case 3:
                    manager.filterByMultipleCriteria(scanner);
                    break;

                case 4:
                    System.out.println("🧾 Generating Trial Balance Report...");
                    BalanceCalculator calc = new TrialBalanceCalculator();
                    ReportPrinter printer = new ReportPrinter(calc);
                    printer.printReport(entries);
                    break;

                case 0:
                    System.out.println("👋 Exiting... Goodbye!");
                    return;

                default:
                    System.out.println("❌ Invalid choice. Try again.");
            }
        }
    }
}
