package main;

import model.*;
import manager.UnifiedJournalManager;
import services.BalanceCalculator;
import services.ReportPrinter;
import services.TrialBalanceCalculator;
import services.GeneralLedger;
import utils.InputValidator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ArrayList<JournalEntry> entries = new ArrayList<>();
        UnifiedJournalManager manager = new UnifiedJournalManager(entries);
        GeneralLedger generalLedger = new GeneralLedger();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            displayMenu();
            int choice = getUserChoice(scanner);

            switch (choice) {
                case 1:
                    addJournalEntry(scanner, manager);
                    break;
                case 2:
                    manager.displayAllEntries();
                    break;
                case 3:
                    manager.filterByMultipleCriteria(scanner);
                    break;
                case 4:
                    generateTrialBalanceReport(entries);
                    break;
                case 5:
                    generateGeneralLedgerReport(entries, generalLedger);
                    break;
                case 6:
                    viewSingleAccountLedger(entries, generalLedger, scanner);
                    break;
                case 0:
                    System.out.println("\uD83D\uDC4B Exiting... Goodbye!");
                    return;
                default:
                    System.out.println("\u274C Invalid choice. Try again.");
            }
        }
    }

    private static void displayMenu() {
        System.out.println("\n\uD83D\uDCCB Journal Entry System");
        System.out.println("1. ➕ Add Entry");
        System.out.println("2. 📘 Display All Entries");
        System.out.println("3. 🔍 Filter by Status + Creator + Min Amount");
        System.out.println("4. 🧾 Generate Trial Balance Report");
        System.out.println("5. 📒 Generate General Ledger (all accounts)");
        System.out.println("6. 📄 View Single Account Ledger");
        System.out.println("0. ❌ Exit");
    }

    private static int getUserChoice(Scanner scanner) {
        System.out.print("👉 Choose an option: ");
        return Integer.parseInt(scanner.nextLine());
    }

    private static void addJournalEntry(Scanner scanner, UnifiedJournalManager manager) {
        String transactionID = getTransactionID(scanner);
        LocalDate date = getTransactionDate(scanner);
        String debitAccount = getAccount(scanner, "Debit");
        String creditAccount = getAccount(scanner, "Credit");
        double amount = getTransactionAmount(scanner);
        String createdBy = getCreator(scanner);
        String status = getStatus(scanner);

        DebitTransaction debit = new DebitTransaction(debitAccount, amount);
        CreditTransaction credit = new CreditTransaction(creditAccount, amount);
        JournalEntry entry = new JournalEntry(transactionID, date, debit, credit, createdBy, status);
        manager.addEntry(entry);

        System.out.println("✅ Journal Entry Added!");
    }

    private static String getTransactionID(Scanner scanner) {
        System.out.print("🆔 Enter Transaction ID: ");
        return scanner.nextLine();
    }

    private static LocalDate getTransactionDate(Scanner scanner) {
        while (true) {
            System.out.print("📅 Enter Date (YYYY-MM-DD): ");
            try {
                LocalDate date = LocalDate.parse(scanner.nextLine());
                if (InputValidator.isValidDate(date)) return date;
                System.out.println("❌ Date cannot be in the future.");
            } catch (Exception e) {
                System.out.println("❌ Invalid format. Try again.");
            }
        }
    }

    private static String getAccount(Scanner scanner, String accountType) {
        while (true) {
            System.out.printf("🏦 Enter %s Account: ", accountType);
            String account = scanner.nextLine();
            if (InputValidator.isValidAccount(account)) return account;
            System.out.println("❌ Invalid account name.");
        }
    }

    private static double getTransactionAmount(Scanner scanner) {
        while (true) {
            System.out.print("💰 Enter Transaction Amount: ");
            try {
                double amount = Double.parseDouble(scanner.nextLine());
                if (InputValidator.isValidAmount(amount)) return amount;
                System.out.println("❌ Amount must be > 0.");
            } catch (Exception e) {
                System.out.println("❌ Invalid amount format.");
            }
        }
    }

    private static String getCreator(Scanner scanner) {
        while (true) {
            System.out.print("👤 Enter Created By: ");
            String name = scanner.nextLine();
            if (InputValidator.isValidAccount(name)) return name;
            System.out.println("❌ Invalid name.");
        }
    }

    private static String getStatus(Scanner scanner) {
        while (true) {
            System.out.print("✅ Enter Status (approved/pending): ");
            String status = scanner.nextLine().trim();
            if (InputValidator.isValidStatus(status)) return status;
            System.out.println("❌ Must be 'approved' or 'pending'.");
        }
    }

    private static void generateTrialBalanceReport(ArrayList<JournalEntry> entries) {
        System.out.println("🧾 Generating Trial Balance Report...");
        BalanceCalculator calc = new TrialBalanceCalculator();
        ReportPrinter printer = new ReportPrinter(calc);
        printer.printReport(entries);
    }

    private static void generateGeneralLedgerReport(ArrayList<JournalEntry> entries, GeneralLedger generalLedger) {
        System.out.println("📒 Generating General Ledger...");
        generalLedger.build(entries);
        generalLedger.printAll();
    }

    private static void viewSingleAccountLedger(ArrayList<JournalEntry> entries, GeneralLedger generalLedger, Scanner scanner) {
        generalLedger.build(entries);
        System.out.print("🔎 Enter account name: ");
        String account = scanner.nextLine().trim();
        generalLedger.printAccount(account);
    }
}
