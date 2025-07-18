# 🧾 Java Accounting System – Double Entry Journal (OOP Project)

This is a beginner-to-intermediate Java project that simulates a simple **double-entry accounting system**.  
It allows users to create, view, filter, and print trial balances using solid Object-Oriented Programming (OOP) principles.

---

## 📌 Features

- ✅ Create debit & credit journal entries
- ✅ Validate date, amount, and names
- ✅ Filter entries by:
    - Status
    - Creator
    - Minimum Amount
- ✅ Display all entries clearly
- ✅ Calculate and print Trial Balance with totals
- ✅ Check if system is balanced (debit = credit)
- ✅ Menu-based console UI

---

## 🛠️ Tech Stack

- Language: **Java**
- IDE: **IntelliJ IDEA**
- Paradigm: **Object-Oriented Programming (OOP)**

---

## 🧠 OOP Principles Used

| Principle       | Description                                                                 |
|----------------|-----------------------------------------------------------------------------|
| **Abstraction** | `BalanceCalculator` is an interface (contract) implemented by `TrialBalanceCalculator` |
| **Inheritance** | `DebitTransaction` and `CreditTransaction` inherit from the abstract class `Transaction` |
| **Encapsulation** | All fields in model classes are private, accessed via getters/setters         |
| **Polymorphism** | `display()` method is overridden in `DebitTransaction` and `CreditTransaction` |

---

## 🧱 Interface: `BalanceCalculator`

```java
// 📄 OOP Concept: Abstraction
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
```

---

## 🗂️ Class Structure

| Class                    | Responsibility                                                              |
|--------------------------|------------------------------------------------------------------------------|
| `Main`                   | User interface: menu, input, and interaction                                |
| `JournalEntry`           | Combines debit & credit into one accounting unit                            |
| `Transaction`            | Abstract class for shared debit/credit behavior                             |
| `DebitTransaction`       | Inherits from `Transaction`, represents debit side                          |
| `CreditTransaction`      | Inherits from `Transaction`, represents credit side                         |
| `UnifiedJournalManager`  | Handles storing, displaying, and filtering journal entries                  |
| `InputValidator`         | Validates all user input                                                    |
| `BalanceCalculator`      | Interface defining trial balance contract (abstraction)                     |
| `TrialBalanceCalculator` | Implements balance logic for trial balance (calculate + display + check)    |
| `ReportPrinter`          | Prints the trial balance using any `BalanceCalculator` implementation       |

---

## 🧪 Sample Menu (Console)

```text
📋 Journal Entry System
1. ➕ Add Entry
2. 📘 Display All Entries
3. 🔍 Filter by Status + Creator + Min Amount
4. 📊 Print Trial Balance
0. ❌ Exit
```

---

## 📎 Author

**Jawad Berjawi**  
GitHub: [jawadberjawi](https://github.com/jawadberjawi)  
LinkedIn: [LinkedIn Profile](https://www.linkedin.com/in/jawad-berjawi-8558ab370)

---

## ✅ Status

This project is part of a larger **Java Accounting System** that is being built over 12 weeks, step-by-step.  
It will evolve into a full portfolio-ready backend system using **Spring Boot**, **SQL**, and clean architecture.

