package services;

import model.*;
import model.CreditTransaction;
import model.DebitTransaction;
import model.JournalEntry;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

/**
 * Builds an Income Statement from your JournalEntry list (no changes to existing code).
 * - Includes only approved entries within [from, to]
 * - Classifies accounts via a small chart + fallbacks
 * - Applies correct debit/credit logic by account type
 */
public class IncomeStatementService {
    private final Map<String, AccountType> chart; // normalized name -> type

    public IncomeStatementService(Map<String, AccountType> chartOfAccounts) {
        this.chart = new HashMap<>();
        if (chartOfAccounts != null) {
            for (var e : chartOfAccounts.entrySet()) {
                this.chart.put(normalize(e.getKey()), e.getValue());
            }
        }
        // Sensible defaults (only if not provided)
        addDefaultIfMissing("service revenue", AccountType.REVENUE);
        addDefaultIfMissing("sales revenue", AccountType.REVENUE);
        addDefaultIfMissing("interest income", AccountType.REVENUE);
        addDefaultIfMissing("sales returns", AccountType.CONTRA_REVENUE);
        addDefaultIfMissing("sales allowances", AccountType.CONTRA_REVENUE);
        addDefaultIfMissing("sales discounts", AccountType.CONTRA_REVENUE);
        addDefaultIfMissing("rent expense", AccountType.EXPENSE);
        addDefaultIfMissing("salaries expense", AccountType.EXPENSE);
        addDefaultIfMissing("utilities expense", AccountType.EXPENSE);
        addDefaultIfMissing("depreciation expense", AccountType.EXPENSE);
        addDefaultIfMissing("cogs", AccountType.EXPENSE);
        addDefaultIfMissing("cost of goods sold", AccountType.EXPENSE);
    }

    private void addDefaultIfMissing(String name, AccountType type) {
        this.chart.putIfAbsent(normalize(name), type);
    }

    public IncomeStatement generate(List<JournalEntry> allEntries, LocalDate from, LocalDate to) {
        if (allEntries == null) allEntries = Collections.emptyList();
        if (from == null || to == null) throw new IllegalArgumentException("from/to dates are required");

        Map<String, BigDecimal> revenueLines = new HashMap<>();
        Map<String, BigDecimal> expenseLines = new HashMap<>();
        Set<String> unknowns = new TreeSet<>();

        for (JournalEntry e : allEntries) {
            if (e == null) continue;
            if (!"approved".equalsIgnoreCase(safe(e.getStatus()))) continue;

            LocalDate d = e.getDate();
            if (d == null || d.isBefore(from) || d.isAfter(to)) continue;

            // Debit line
            DebitTransaction dt = e.getDebitTransaction();
            if (dt != null && dt.getAccountName() != null) {
                classifyAndAccumulate(dt.getAccountName(), bd(dt.getAmount()), true, revenueLines, expenseLines, unknowns);
            }

            // Credit line
            CreditTransaction ct = e.getCreditTransaction();
            if (ct != null && ct.getAccountName() != null) {
                classifyAndAccumulate(ct.getAccountName(), bd(ct.getAmount()), false, revenueLines, expenseLines, unknowns);
            }
        }

        BigDecimal totalRevenues = sum(revenueLines.values());
        BigDecimal totalExpenses = sum(expenseLines.values());
        BigDecimal netIncome = totalRevenues.subtract(totalExpenses);

        Map<String, BigDecimal> revSorted = new TreeMap<>(revenueLines);
        Map<String, BigDecimal> expSorted = new TreeMap<>(expenseLines);

        return new IncomeStatement(
                from,
                to,
                revSorted,
                expSorted,
                totalRevenues.setScale(2, RoundingMode.HALF_UP),
                totalExpenses.setScale(2, RoundingMode.HALF_UP),
                netIncome.setScale(2, RoundingMode.HALF_UP),
                new ArrayList<>(unknowns)
        );
    }

    private void classifyAndAccumulate(
            String rawAccount,
            BigDecimal amount,
            boolean isDebit,
            Map<String, BigDecimal> revenueLines,
            Map<String, BigDecimal> expenseLines,
            Set<String> unknowns
    ) {
        String normalized = normalize(rawAccount);
        AccountType type = chart.getOrDefault(normalized, inferType(normalized));

        switch (type) {
            case REVENUE: {
                // Credit increases revenue (+), Debit decreases (−)
                BigDecimal signed = isDebit ? amount.negate() : amount;
                merge(revenueLines, normalized , signed);
                break;
            }
            case CONTRA_REVENUE: {
                // Treat contra-revenue as negative revenue overall
                BigDecimal signed = isDebit ? amount : amount.negate();
                merge(revenueLines, normalized, signed.negate()); // subtract from total revenues
                break;
            }
            case EXPENSE: {
                // Debit increases expense (+), Credit decreases (−)
                BigDecimal signed = isDebit ? amount : amount.negate();
                merge(expenseLines, normalized, signed);
                break;
            }
            case OTHER:
            default:
                unknowns.add(normalized);
        }
    }

    private static void merge(Map<String, BigDecimal> map, String key, BigDecimal delta) {
        map.merge(key, delta, BigDecimal::add);
    }

    private static BigDecimal sum(Collection<BigDecimal> values) {
        BigDecimal t = BigDecimal.ZERO;
        for (BigDecimal v : values) t = t.add(v);
        return t;
    }

    private static String normalize(String s) {
        if (s == null) return "";
        String t = s.trim().toLowerCase();
        t = t.replaceAll("\\s+", " ");
        t = t.replace('–', '-');
        return t;
    }

    private static String displayName(String s) {
        return s == null ? "" : s.trim();
    }

    private static String safe(String s) {
        return s == null ? "" : s;
    }

    private static BigDecimal bd(double d) {
        return BigDecimal.valueOf(d).setScale(2, RoundingMode.HALF_UP);
    }

    /** Fallback naming rules when not found in chart */
    private static AccountType inferType(String name) {
        if (name.endsWith("expense")) return AccountType.EXPENSE;
        if (name.equals("cogs") || name.contains("cost of goods sold")) return AccountType.EXPENSE;
        if (name.endsWith("revenue")) return AccountType.REVENUE;
        if (name.contains("income") && !name.contains("expense")) return AccountType.REVENUE;
        if (name.contains("sales") && (name.contains("return") || name.contains("allowance") || name.contains("discount"))) {
            return AccountType.CONTRA_REVENUE;
        }
        return AccountType.OTHER;
    }
}
