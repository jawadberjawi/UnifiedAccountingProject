# 🧾 Java Accounting System – Double Entry Journal (OOP Project)

This is a beginner-to-intermediate Java project that simulates a simple **double-entry accounting system**. It lets you create journal entries, view/filter them, and print key financial reports (**Trial Balance**, **General Ledger**, **Income Statement**, **Balance Sheet**) using clean OOP design.

---

## 📌 Features
- ✅ Create debit & credit journal entries  
- ✅ Validate date, amount, and names  
- ✅ Filter entries by Status, Creator, and Minimum Amount  
- ✅ Display all entries clearly  
- ✅ **Trial Balance** with totals + balanced check (debit = credit)  
- ✅ **General Ledger** per account with running balances (sorted by date)  
- ✅ **View a single account’s ledger** (drill-down)  
- ✅ **Income Statement**: choose a date range, classify accounts, handle debit/credit signs correctly, and compute Net Income  
- ✅ **Balance Sheet (NEW)**: shows Assets, Liabilities, and Equity (including Retained Earnings / Net Income) as of a chosen date  
- ✅ Menu-based console UI  

---

## 🧾 Balance Sheet (NEW)
The Balance Sheet provides a snapshot of the company’s financial position **as of a specific date**, showing **Assets**, **Liabilities**, and **Equity**.  
It automatically includes the **Net Income** (from the Income Statement) under **Retained Earnings / Net Income**.

**How it works:**
- Filters all approved journal entries up to the selected "as of" date  
- Rebuilds account balances using the `GeneralLedger`  
- Classifies each account as **Asset**, **Liability**, or **Equity** using a chart of accounts + smart inference  
- Integrates **Net Income** from the Income Statement (Jan 1 → as of date)  
- Displays totals and checks whether the Balance Sheet is balanced (Assets = Liabilities + Equity)

**Key Classes:**
- `BalanceSheet`: immutable model representing financial position as of a date  
- `BalanceSheetService`: builds the balance sheet from all journal entries and the income statement  
- `BalanceSheetRenderer`: prints a neat formatted report with totals and balance validation  

**Menu Integration:**  
Choose **option 8** to generate a Balance Sheet and specify the **“As of” date**.

### ✅ Example Output
BALANCE SHEET (As of 2025-10-30)
==================================================
ASSETS
Cash                          10000.00
Equipment                      5000.00
Total ASSETS                  15000.00

LIABILITIES
Accounts Payable               3000.00
Total LIABILITIES              3000.00

EQUITY
Owner's Capital               10000.00
Retained Earnings / Net Income 2000.00
Total EQUITY                  12000.00

--------------------------------------------------
Total Assets                  15000.00
Total Liabilities + Equity    15000.00
✅ Balanced
==================================================


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

## 📈 Income Statement
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

---

## 🗂️ Class Structure
| Class                        | Responsibility                                                             |
|------------------------------|------------------------------------------------------------------------------|
| `Main`                       | User interface: menu, input, and interaction                                |
| `JournalEntry`               | Combines debit & credit into one accounting unit                            |
| `Transaction`                | Abstract class for shared debit/credit behavior                             |
| `DebitTransaction`           | Inherits from `Transaction`, represents debit side                          |
| `CreditTransaction`          | Inherits from `Transaction`, represents credit side                         |
| `UnifiedJournalManager`      | Stores, displays, and filters journal entries                               |
| `GeneralLedger`              | Builds and prints ledger with running balances                              |
| `IncomeStatement`            | Immutable model for income statement data                                   |
| `IncomeStatementService`     | Business logic for classifying accounts and computing revenues/expenses     |
| `IncomeStatementRenderer`    | Console output for income statement                                         |
| `BalanceSheet`               | Immutable model representing financial position as of a date                |
| `BalanceSheetService`        | Builds balance sheet using ledger balances + net income integration         |
| `BalanceSheetRenderer`       | Renders a formatted balance sheet to console                                |
| `InputValidator`             | Validates user input                                                        |
| `BalanceCalculator`          | Interface defining trial balance contract (abstraction)                     |
| `TrialBalanceCalculator`     | Implements balance logic for trial balance                                  |
| `ReportPrinter`              | Prints the trial balance using any `BalanceCalculator` implementation       |
| `LedgerManager` (legacy)     | Old grouped view kept for reference                                         |

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
8. 🧾 Generate Balance Sheet  
0. ❌ Exit  

---

## 🛠️ Tech Stack
- Language: **Java**  
- IDE: **IntelliJ IDEA**  
- Paradigm: **Object-Oriented Programming (OOP)**  

---

## 📎 Author
**Jawad Berjawi**  
GitHub: [github.com/jawadberjawi](https://github.com/jawadberjawi)  
LinkedIn: [linkedin.com/in/jawad-berjawi-8558ab370](https://linkedin.com/in/jawad-berjawi-8558ab370)

---

## ✅ Status
This project is part of a larger **Java Accounting System** being built step-by-step.  
Next phases: **Spring Boot**, **SQL (PostgreSQL + H2)**, **REST APIs**, clean architecture, and basic tests.

