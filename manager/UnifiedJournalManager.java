package manager;

import model.JournalEntry;
import java.util.ArrayList;
import java.util.Scanner;

public class UnifiedJournalManager {

    // 📦 List that stores journal entries
    private ArrayList<JournalEntry> entries;

    // 🔧 Constructor to initialize the entries list
    public UnifiedJournalManager(ArrayList<JournalEntry> entries) {
        this.entries = entries;
    }

    // ➕ Method to add a new journal entry
    public void addEntry(JournalEntry entry) {
        entries.add(entry);
    }

    // 📘 Method to display all journal entries
    public void displayAllEntries() {
        if (entries.isEmpty()) {
            System.out.println("📭 No entries available.");
            return;
        }
        for (JournalEntry entry : entries) {
            entry.display();
        }
    }

    // 🔍 Method to filter entries based on multiple criteria
    public void filterByMultipleCriteria(Scanner input) {
        System.out.println("\n🔍 Available Filter Options:");
        System.out.println("1. Filter by Status");
        System.out.println("2. Filter by Creator");
        System.out.println("3. Filter by Minimum Amount");
        System.out.println("4. Apply All Filters");
        System.out.print("👉 Please select a filter (1-4): ");

        int filterChoice;
        try {
            filterChoice = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("⚠️ Invalid selection. Defaulting to apply all filters.");
            filterChoice = 4;
        }

        // 🔍 Variables to hold user input for filters
        String status = null;
        String createdBy = null;
        Double minAmount = null;

        // Filter by status
        if (filterChoice == 1 || filterChoice == 4) {
            System.out.print("Enter Status to filter by (approved/pending/rejected): ");
            status = input.nextLine().trim();
            if (status.isEmpty()) status = null;
        }

        // Filter by creator
        if (filterChoice == 2 || filterChoice == 4) {
            System.out.print("Enter Creator to filter by: ");
            createdBy = input.nextLine().trim();
            if (createdBy.isEmpty()) createdBy = null;
        }

        // Filter by minimum amount
        if (filterChoice == 3 || filterChoice == 4) {
            System.out.print("Enter Minimum Amount to filter by: ");
            String amountInput = input.nextLine().trim();
            if (!amountInput.isEmpty()) {
                try {
                    minAmount = Double.parseDouble(amountInput);
                } catch (NumberFormatException e) {
                    System.out.println("⚠️ Invalid amount input. Skipping the amount filter.");
                }
            }
        }

        // 🔁 Loop through entries and apply filters
        boolean found = false;
        for (JournalEntry entry : entries) {
            boolean match = true;

            if (status != null && !entry.getStatus().equalsIgnoreCase(status)) match = false;
            if (createdBy != null && !entry.getCreatedBy().equalsIgnoreCase(createdBy)) match = false;
            if (minAmount != null && entry.getAmount() < minAmount) match = false;

            if (match) {
                entry.display();
                found = true;
            }
        }

        // 📨 Display message if no matches found
        if (!found) {
            System.out.println("📭 No entries found matching the selected filters.");
        }
    }
}
