# 🧾 Java Accounting System – Double Entry Journal (OOP Project)

This is a beginner-to-intermediate Java project that simulates a simple **double-entry accounting system**. It lets you create journal entries, view/filter them, and print key financial reports (Trial Balance, General Ledger, Income Statement) using clean OOP design.

---

## 📌 Features
- ✅ Create debit & credit journal entries
- ✅ Validate date, amount, and names
- ✅ Filter entries by Status, Creator, and Minimum Amount
- ✅ Display all entries clearly
- ✅ **Trial Balance** with totals + balanced check (debit = credit)
- ✅ **General Ledger** per account with running balances (sorted by date)
- ✅ **View a single account’s ledger** (drill-down)
- ✅ **Income Statement (NEW)**: choose a date range, classify accounts (Revenue, Expense, Contra-Revenue), handle debit/credit signs correctly, sum totals, and compute Net Income
- ✅ Menu-based console UI

---

## 🛠️ Tech Stack
- Language: **Java**
- IDE: **IntelliJ IDEA**
- Paradigm: **Object-Oriented Programming (OOP)**

---

## 🧠 OOP Principles Used
| Principle        | Description                                                                 |
|------------------|-----------------------------------------------------------------------------|
| **Abstraction**  | `BalanceCalculator` is an interface implemented by `TrialBalanceCalculator` |
| **Inheritance**  | `DebitTransaction` and `CreditTransaction` inherit from `Transaction`       |
| **Encapsulation**| Model fields are private with getters/setters                               |
| **Polymorphism** | Calculators can be swapped via the `BalanceCalculator` interface            |

---

## 📘 General Ledger (Running Balances)
`GeneralLedger` builds a ledger per account, sorts by date, and computes a running balance line-by-line.

**What it does:**
- Expands each `JournalEntry` (debit = +, credit = −)
- Groups by account and sorts by date (stable tiebreak by transaction ID)
- Prints **Debit / Credit / Balance** with neat columns
- Shows totals + closing balance for each account

**Supports:**
- `printAll()` – all accounts
- `printAccount("Cash")` – a single account
- `getAccounts()` – list of accounts
- `getFinalBalance("Cash")` – closing balance

---

## 📈 Income Statement (NEW)
The Income Statement aggregates **Revenues − Expenses = Net Income** over a date range.

**How it works:**
- Includes only **approved** entries within `[from, to]`
- Classifies accounts via a small Chart of Accounts (`Map<String, AccountType>`) + smart fallbacks (`inferType`)
- Handles **Contra-Revenue** correctly (e.g., Sales Returns/Allowances/Discounts reduce total revenue)
- Applies proper debit/credit sign rules per account type
- Uses `BigDecimal` with scale 2 and **HALF_UP** rounding
- Renders a clean console report (sorted alphabetically)

**Key Classes:**
- `AccountType` (enum): `REVENUE, EXPENSE, CONTRA_REVENUE, OTHER`
- `IncomeStatement`: immutable data model (from/to, revenues, expenses, totals, net income, ignored accounts)
- `IncomeStatementService`: builds the statement from `List<JournalEntry>` using chart + fallbacks
- `IncomeStatementRenderer`: prints a tidy report to console

**Menu Integration:**  
Choose **option 7** to generate the Income Statement, enter a date range, and the report will display Revenues, Expenses, Totals, Net Income, and any ignored (non P&L) accounts.

### ✅ Example Output
Income Statement  
For the period: 2025-08-01 to 2025-08-31  
==================================================  
Revenues  
Service Revenue                 1000.00  
Total Revenues                  1000.00

Expenses  
Rent Expense                     200.00  
Total Expenses                   200.00

Net Income                        800.00
==================================================  
Note: Ignored (not revenue/expense): Owner's Capital, Cash

---

## 🗂️ Class Structure
| Class                    | Responsibility                                                              |
|--------------------------|------------------------------------------------------------------------------|
| `Main`                   | User interface: menu, input, and interaction                                |
| `JournalEntry`           | Combines debit & credit into one accounting unit                            |
| `Transaction`            | Abstract class for shared debit/credit behavior                             |
| `DebitTransaction`       | Inherits from `Transaction`, represents debit side                          |
| `CreditTransaction`      | Inherits from `Transaction`, represents credit side                         |
| `UnifiedJournalManager`  | Stores, displays, and filters journal entries                               |
| `GeneralLedger`          | Builds and prints ledger with running balances                              |
| `IncomeStatement`        | Immutable model for income statement data                                   |
| `IncomeStatementService` | Business logic for classifying accounts and computing revenues/expenses     |
| `IncomeStatementRenderer`| Console output for income statement                                         |
| `InputValidator`         | Validates user input                                                        |
| `BalanceCalculator`      | Interface defining trial balance contract (abstraction)                     |
| `TrialBalanceCalculator` | Implements balance logic for trial balance                                  |
| `ReportPrinter`          | Prints the trial balance using any `BalanceCalculator` implementation       |
| `LedgerManager` (legacy) | Old, simple grouped view (kept for reference; not used by `Main`)           |

---

## 🧪 Sample Menu (Console)
📋 Journal Entry System
1. ➕ Add Entry
2. 📘 Display All Entries
3. 🔍 Filter by Status + Creator + Min Amount
4. 🧾 Generate Trial Balance Report
5. 📒 Generate General Ledger (all accounts)
6. 📄 View Single Account Ledger
7. 📈 Generate Income Statement
0. ❌ Exit

---

## 📎 Author
**Jawad Berjawi**  
GitHub: [github.com/jawadberjawi](https://github.com/jawadberjawi)  
LinkedIn: [linkedin.com/in/jawad-berjawi-8558ab370](https://www.linkedin.com/in/jawad-berjawi-8558ab370)

---

## ✅ Status
This project is part of a larger **Java Accounting System** being built step-by-step. Next phases: **Spring Boot**, **SQL (PostgreSQL + H2)**, REST APIs, clean architecture, and basic tests.

