package manager;

import model.JournalEntry;
import java.util.ArrayList;
import java.util.Scanner;

public class UnifiedJournalManager {
    private ArrayList<JournalEntry> entries;

    public UnifiedJournalManager(ArrayList<JournalEntry> entries) {
        this.entries = entries;
    }

    public void addEntry(JournalEntry entry) {
        entries.add(entry);
    }

    public void displayAllEntries() {
        if (entries.isEmpty()) {
            System.out.println("📭 No entries found.");
            return;
        }
        for (JournalEntry entry : entries) {
            entry.display();
        }
    }

    public void filterByMultipleCriteria(Scanner input) {
        System.out.println("\n🔍 Filter Options:");
        System.out.println("1. Filter by Status");
        System.out.println("2. Filter by Creator");
        System.out.println("3. Filter by Minimum Amount");
        System.out.println("4. Apply All Filters");
        System.out.print("👉 Choose filtering method (1-4): ");

        int filterChoice;
        try {
            filterChoice = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("⚠️ Invalid choice. Using all filters by default.");
            filterChoice = 4;
        }

        String status = null;
        String createdBy = null;
        Double minAmount = null;

        // Only prompt for criteria that will be used
        if (filterChoice == 1 || filterChoice == 4) {
            System.out.print("Enter Status to filter (approved/pending/rejected): ");
            status = input.nextLine().trim();
            if (status.isEmpty()) status = null;
        }

        if (filterChoice == 2 || filterChoice == 4) {
            System.out.print("Enter Created By to filter: ");
            createdBy = input.nextLine().trim();
            if (createdBy.isEmpty()) createdBy = null;
        }

        if (filterChoice == 3 || filterChoice == 4) {
            System.out.print("Enter Minimum Amount to filter: ");
            String minAmountInput = input.nextLine().trim();
            if (!minAmountInput.isEmpty()) {
                try {
                    minAmount = Double.parseDouble(minAmountInput);
                } catch (NumberFormatException e) {
                    System.out.println("⚠️ Invalid amount. Skipping amount filter.");
                }
            }
        }

        boolean found = false;
        for (JournalEntry entry : entries) {
            boolean match = true;

            // Apply only the selected filters
            if (status != null && !entry.getStatus().equalsIgnoreCase(status)) {
                match = false;
            }
            if (createdBy != null && !entry.getCreatedBy().equalsIgnoreCase(createdBy)) {
                match = false;
            }
            if (minAmount != null && entry.getAmount() < minAmount) {
                match = false;
            }

            if (match) {
                entry.display();
                found = true;
            }
        }

        if (!found) {
            System.out.println("📭 No matching entries found with the selected filters.");
        }
    }


}
