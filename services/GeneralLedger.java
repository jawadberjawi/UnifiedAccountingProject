package services;

import model.JournalEntry;
import model.DebitTransaction;
import model.CreditTransaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * General Ledger (دفتر الأستاذ) with running balances.
 * - build(entries): group by account, sort by date, compute running balance
 * - printAll(): print all accounts
 * - printAccount(name): print a single account
 * - getAccounts(): list account names (alphabetical)
 * - getFinalBalance(name): last running balance for account
 * - clear(): reset internal state
 *
 * This class is read-only over your JournalEntry list (doesn't modify entries).
 */
public class GeneralLedger {

    // Keep accounts alphabetically ordered for nice printing.
    private final Map<String, List<LedgerLine>> ledger = new TreeMap<>();

    // Column width for printing amounts neatly.
    private static final int WIDTH = 12;

    /**
     * Build (or rebuild) the general ledger structure from raw journal entries.
     * Groups by account, sorts each account's lines by date, and computes running balance.
     */
    public void build(List<JournalEntry> entries) {
        ledger.clear();
        if (entries == null || entries.isEmpty()) return;

        // 1) Expand entries into per-account lines (debit: +amount, credit: -amount).
        for (JournalEntry e : entries) {
            if (e == null) continue;

            LocalDate date = e.getDate();
            // Defensive: if date is null, put it last by using LocalDate.MAX for ordering later.
            LocalDate safeDate = (date != null) ? date : LocalDate.MAX;

            // Debit side → +amount for that account
            DebitTransaction d = e.getDebitTransaction();
            if (d != null && d.getAccountName() != null) {
                addLine(
                        d.getAccountName().trim(),
                        safeDate,
                        BigDecimal.valueOf(d.getAmount()),   // debit amount
                        BigDecimal.ZERO,                      // credit amount
                        +1,                                   // sign for delta (+)
                        e                                     // reference back to entry if needed
                );
            }

            // Credit side → -amount for that account
            CreditTransaction c = e.getCreditTransaction();
            if (c != null && c.getAccountName() != null) {
                addLine(
                        c.getAccountName().trim(),
                        safeDate,
                        BigDecimal.ZERO,                      // debit amount
                        BigDecimal.valueOf(c.getAmount()),    // credit amount
                        -1,                                   // sign for delta (−)
                        e
                );
            }
        }

        // 2) Sort each account’s lines by date (and by transaction id as tiebreaker for stability).
        for (Map.Entry<String, List<LedgerLine>> accountEntry : ledger.entrySet()) {
            List<LedgerLine> lines = accountEntry.getValue();
            lines.sort((a, b) -> {
                int cmp = a.date.compareTo(b.date);
                if (cmp != 0) return cmp;
                // Tie-breaker: by transaction id if available (nulls last)
                String ta = a.entryRef != null ? a.entryRef.getTransactionID() : null;
                String tb = b.entryRef != null ? b.entryRef.getTransactionID() : null;
                if (ta == null && tb == null) return 0;
                if (ta == null) return 1;
                if (tb == null) return -1;
                return ta.compareTo(tb);
            });

            // 3) Compute running balance: start from zero
            BigDecimal running = BigDecimal.ZERO;
            for (LedgerLine line : lines) {
                // delta = +debit or -credit (already applied when created)
                running = running.add(line.delta);
                line.runningBalance = running;
            }
        }
    }

    /**
     * Print the full general ledger (all accounts).
     */
    public void printAll() {
        if (ledger.isEmpty()) {
            System.out.println("📭 General Ledger is empty.");
            return;
        }

        System.out.println("\n📒 General Ledger (All Accounts)");
        System.out.println("================================");

        for (String account : ledger.keySet()) {
            printAccount(account);
        }
    }

    /**
     * Print a single account’s ledger with running balance.
     */
    public void printAccount(String accountName) {
        List<LedgerLine> lines = ledger.get(accountName);
        if (lines == null || lines.isEmpty()) {
            System.out.println("\nNo entries for account: " + accountName);
            return;
        }

        System.out.println("\nAccount: " + accountName);
        System.out.println("Date       | Debit        | Credit       | Balance");
        System.out.println("---------------------------------------------------");

        for (LedgerLine line : lines) {
            String date = (line.date == null || line.date.equals(LocalDate.MAX))
                    ? "N/A"
                    : line.date.toString();

            String debitStr  = (line.debit.signum()  == 0) ? "" : fmt(line.debit);
            String creditStr = (line.credit.signum() == 0) ? "" : fmt(line.credit);
            String balStr    = fmt(line.runningBalance);

            System.out.println(
                    pad(date, 10) + " | " +
                            padLeft(debitStr,  WIDTH) + " | " +
                            padLeft(creditStr, WIDTH) + " | " +
                            padLeft(balStr,    WIDTH)
            );
        }

        // Totals (optional nice touch)
        BigDecimal totalDebit  = BigDecimal.ZERO;
        BigDecimal totalCredit = BigDecimal.ZERO;
        for (LedgerLine line : lines) {
            totalDebit  = totalDebit.add(line.debit);
            totalCredit = totalCredit.add(line.credit);
        }
        System.out.println("---------------------------------------------------");
        System.out.println(
                pad("Totals", 10) + " | " +
                        padLeft(fmt(totalDebit),  WIDTH) + " | " +
                        padLeft(fmt(totalCredit), WIDTH) + " | " +
                        padLeft(fmt(lines.get(lines.size()-1).runningBalance), WIDTH)
        );
    }

    /**
     * Get the list of account names (alphabetical).
     */
    public Set<String> getAccounts() {
        return new LinkedHashSet<>(ledger.keySet());
    }

    /**
     * Get the final (closing) running balance for an account.
     */
    public BigDecimal getFinalBalance(String accountName) {
        List<LedgerLine> lines = ledger.get(accountName);
        if (lines == null || lines.isEmpty()) return BigDecimal.ZERO;
        return lines.get(lines.size() - 1).runningBalance;
    }

    /**
     * Clear internal state (rebuild from scratch later).
     */
    public void clear() {
        ledger.clear();
    }

    /* ------------------- internals ------------------- */

    private void addLine(String account, LocalDate date,
                         BigDecimal debit, BigDecimal credit,
                         int sign, JournalEntry ref) {
        // delta = +debit or -credit
        BigDecimal delta = (sign > 0) ? debit : credit.negate();

        List<LedgerLine> list = ledger.computeIfAbsent(account, k -> new ArrayList<>());
        list.add(new LedgerLine(date, debit, credit, delta, ref));
    }

    // Nice 2-decimal formatting without forcing locale here.
    private String fmt(BigDecimal v) {
        return v.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
    }

    // Simple padding helpers for console output.
    private static String pad(String s, int width) {
        if (s == null) s = "";
        if (s.length() >= width) return s;
        return s + " ".repeat(width - s.length());
    }
    private static String padLeft(String s, int width) {
        if (s == null) s = "";
        if (s.length() >= width) return s;
        return " ".repeat(width - s.length()) + s;
    }

    /**
     * One printed row in the General Ledger for a specific account.
     * debit and credit are non-negative; delta is applied sign (+ or -).
     */
    private static class LedgerLine {
        final LocalDate date;
        final BigDecimal debit;           // >= 0
        final BigDecimal credit;          // >= 0
        final BigDecimal delta;           // +debit or -credit
        final JournalEntry entryRef;      // reference to original entry (optional for future)
        BigDecimal runningBalance;        // balance AFTER applying this line

        LedgerLine(LocalDate date, BigDecimal debit, BigDecimal credit,
                   BigDecimal delta, JournalEntry entryRef) {
            this.date = date;
            this.debit = debit;
            this.credit = credit;
            this.delta = delta;
            this.entryRef = entryRef;
            this.runningBalance = BigDecimal.ZERO;
        }
    }
}
