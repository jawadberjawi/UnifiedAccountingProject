# 🧾 Java Accounting System – Double Entry Journal (OOP Project)

This is a beginner-to-intermediate Java project that simulates a simple **double-entry accounting system**.  
It allows users to create, view, filter, and print financial reports using solid Object-Oriented Programming (OOP) principles.

---

## 📌 Features

- ✅ Create debit & credit journal entries
- ✅ Validate date, amount, and names
- ✅ Filter entries by:
  - Status
  - Creator
  - Minimum Amount
- ✅ Display all entries clearly
- ✅ Calculate and print **Trial Balance** with totals
- ✅ Check if system is balanced (debit = credit)
- ✅ **General Ledger with running balances** (grouped by account, sorted by date)
- ✅ **View a single account’s ledger** (drill-down)
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
| **Encapsulation**| Model fields are private with getters/setters                                |
| **Polymorphism** | Calculators can be swapped via the `BalanceCalculator` interface            |

---

## 📘 GeneralLedger – New Feature (Running Balances)

`GeneralLedger` builds a ledger per account, **sorts by date**, and computes a **running balance** line-by-line.

### 🔹 What it does:
- Expands each `JournalEntry` to the affected accounts (debit = +, credit = −)
- Groups by account and sorts by date (stable tiebreak by transaction ID)
- Prints **Debit / Credit / Balance** per line with neat column widths
- Shows **Totals** and the **final (closing) balance** for each account
- Supports:
  - `printAll()` – all accounts
  - `printAccount("Cash")` – a single account
  - `getAccounts()` – list accounts
  - `getFinalBalance("Cash")` – closing balance

### ✅ Example Output:
Account: Cash
Date       | Debit        | Credit       | Balance
---------------------------------------------------
2025-07-07 |       500.00 |              |     500.00
2025-07-07 |              |      200.00  |     300.00
---------------------------------------------------
Totals     |       500.00 |      200.00  |     300.00

> Tip: You can also drill down to a single account from the menu.

---

## 🧱 Interface: BalanceCalculator

// 📄 OOP Concept: Abstraction
package services;

import model.JournalEntry;
import java.util.List;

public interface BalanceCalculator {
void calculate(List<JournalEntry> entries);
void displayResult();
boolean isBalanced();
}

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
| `GeneralLedger`          | **Builds and prints ledger with running balances**                          |
| `InputValidator`         | Validates all user input                                                    |
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
0. ❌ Exit

---

## 📎 Author

**Jawad Berjawi**  
GitHub: [jawadberjawi](https://github.com/jawadberjawi)  
LinkedIn: [LinkedIn Profile](https://www.linkedin.com/in/jawad-berjawi-8558ab370)

---

### ✅ Status

This project is part of a larger **Java Accounting System** being built step-by-step.  
Next phases: **Spring Boot**, **SQL (PostgreSQL + H2)**, REST APIs, clean architecture, and basic tests.
