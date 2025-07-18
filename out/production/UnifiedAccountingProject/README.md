# 🧾 Java Accounting System – Double Entry Journal (OOP Project)

This is a beginner-to-intermediate Java project that simulates a simple **double-entry accounting system**.  
It allows users to create, view, and filter journal entries using Object-Oriented Programming principles.

---

## 📌 Features

- ✅ Create Debit & Credit transactions
- ✅ Combine both into a journal entry
- ✅ Validate user input and balance
- ✅ Filter entries by:
    - Status
    - Creator
    - Minimum Amount
- ✅ Display all entries in a readable format
- ✅ Menu-based console UI

---

## 🛠️ Tech Stack

- Language: **Java**
- IDE: **IntelliJ IDEA**
- Paradigm: **Object-Oriented Programming (OOP)**

---

## 🧠 OOP Concepts Used

- **Abstraction** → `Transaction` is an abstract base class
- **Inheritance** → `DebitTransaction` and `CreditTransaction` extend `Transaction`
- **Encapsulation** → Fields are private with getters
- **Polymorphism** → Each transaction overrides its `display()` method

---

## 🗂️ Class Structure

| Class                  | Role                                                                 |
|------------------------|----------------------------------------------------------------------|
| `Transaction`          | Abstract base class for all transactions                             |
| `DebitTransaction`     | Child class for debit-specific logic                                 |
| `CreditTransaction`    | Child class for credit-specific logic                                |
| `JournalEntry`         | Combines debit & credit into a single accounting entry               |
| `InputValidator`       | Handles all input validation and error handling                      |
| `UnifiedJournalManager`| Stores, displays, and filters journal entries                        |
| `Main`                 | User interface and menu-based interaction                            |

---

## 🧪 Sample Run

```text
1. Create New Journal Entry
2. Display All Entries
3. Filter by Status
4. Filter by Creator
5. Filter by Minimum Amount
6. Exit
