package main;

import model.*;
import manager.UnifiedJournalManager;
import services.BalanceCalculator;
import services.ReportPrinter;
import services.TrialBalanceCalculator;
import services.GeneralLedger;
import services.IncomeStatementService;
import services.IncomeStatementRenderer;
import utils.InputValidator;
import services.BalanceSheetService;
import services.BalanceSheetRenderer;
import model.BalanceSheet;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class  Main {
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
                case 7:
                    generateIncomeStatementReport(entries, scanner);
                    break;
                case 8:
                    generateBalanceSheetReport(entries, generalLedger, scanner);
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
        System.out.println("7. 📈 Generate Income Statement");
        System.out.println("8. 🧾 Generate Balance Sheet");
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

    // ===============================
    // Income Statement Integration 👇
    // ===============================

    private static void generateIncomeStatementReport(ArrayList<JournalEntry> entries, Scanner scanner) {
        if (entries.isEmpty()) {
            System.out.println("ℹ️ No journal entries yet. Add some entries first.");
            return;
        }

        System.out.println("📈 Generate Income Statement");
        LocalDate from = askDate(scanner, "From (YYYY-MM-DD)");
        LocalDate to   = askDate(scanner, "To   (YYYY-MM-DD)");

        if (to.isBefore(from)) {
            System.out.println("❌ 'To' date cannot be before 'From' date.");
            return;
        }

        // Build a simple Chart of Accounts (you can expand this any time)
        Map<String, AccountType> chart = buildDefaultChart();

        IncomeStatementService svc = new IncomeStatementService(chart);
        var is = svc.generate(entries, from, to);

        IncomeStatementRenderer.render(is);
    }

    private static LocalDate askDate(Scanner scanner, String label) {
        while (true) {
            System.out.print("📅 Enter " + label + ": ");
            try {
                LocalDate date = LocalDate.parse(scanner.nextLine().trim());
                if (InputValidator.isValidDate(date)) return date;
                System.out.println("❌ Date cannot be in the future.");
            } catch (Exception e) {
                System.out.println("❌ Invalid format. Try again.");
            }
        }
    }

    private static Map<String, AccountType> buildDefaultChart() {
        Map<String, AccountType> chart = new HashMap<>();
        // Revenues
        chart.put("Service Revenue", AccountType.REVENUE);
        chart.put("Sales Revenue", AccountType.REVENUE);
        chart.put("Interest Income", AccountType.REVENUE);
        // Contra-Revenues (reduce revenue)
        chart.put("Sales Returns", AccountType.CONTRA_REVENUE);
        chart.put("Sales Allowances", AccountType.CONTRA_REVENUE);
        chart.put("Sales Discounts", AccountType.CONTRA_REVENUE);
        // Expenses
        chart.put("Rent Expense", AccountType.EXPENSE);
        chart.put("Salaries Expense", AccountType.EXPENSE);
        chart.put("Utilities Expense", AccountType.EXPENSE);
        chart.put("Depreciation Expense", AccountType.EXPENSE);
        chart.put("COGS", AccountType.EXPENSE);
        chart.put("Cost of Goods Sold", AccountType.EXPENSE);
        // You can add more here, or rely on the service's inferType() fallbacks
        return chart;
    }
    private static void generateBalanceSheetReport(ArrayList<JournalEntry> entries,
                                                   GeneralLedger generalLedger,
                                                   Scanner scanner) {
        if (entries.isEmpty()) {
            System.out.println("ℹ️ No journal entries yet. Add some entries first.");
            return;
        }

        System.out.println("🧾 Generate Balance Sheet");
        LocalDate asOf = askDate(scanner, "As of (YYYY-MM-DD)");

        // Reuse the same chart for classification (Revenue/Expense/Etc.)
        Map<String, AccountType> chart = buildDefaultChart();

        // 1️⃣ Automatically generate Income Statement for same fiscal period (Jan 1 → asOf)
        LocalDate startOfYear = asOf.withDayOfYear(1);
        IncomeStatementService isService = new IncomeStatementService(chart);
        var incomeStatement = isService.generate(entries, startOfYear, asOf);

        // 2️⃣ Build a simple Balance Sheet chart (Account Name → Classification)
        Map<String, BalanceSheetService.BSClass> bsChart = new HashMap<>();
        bsChart.put("Cash", BalanceSheetService.BSClass.ASSET);
        bsChart.put("Bank", BalanceSheetService.BSClass.ASSET);
        bsChart.put("Accounts Receivable", BalanceSheetService.BSClass.ASSET);
        bsChart.put("Inventory", BalanceSheetService.BSClass.ASSET);
        bsChart.put("Equipment", BalanceSheetService.BSClass.ASSET);
        bsChart.put("Accumulated Depreciation", BalanceSheetService.BSClass.CONTRA_ASSET);
        bsChart.put("Accounts Payable", BalanceSheetService.BSClass.LIABILITY);
        bsChart.put("Notes Payable", BalanceSheetService.BSClass.LIABILITY);
        bsChart.put("Owner's Equity", BalanceSheetService.BSClass.EQUITY);
        bsChart.put("Capital", BalanceSheetService.BSClass.EQUITY);
        bsChart.put("Retained Earnings", BalanceSheetService.BSClass.EQUITY);

        // 3️⃣ Generate Balance Sheet as of this date (including net income)
        BalanceSheetService bsService = new BalanceSheetService(bsChart, generalLedger);
        BalanceSheet balanceSheet = bsService.generate(entries, asOf, incomeStatement);

        // 4️⃣ Render results
        BalanceSheetRenderer.render(balanceSheet);
    }



}

