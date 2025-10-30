package services;

import model.BalanceSheet;
import model.IncomeStatement;
import model.JournalEntry;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

public class BalanceSheetService {

    /** Classification buckets for Balance Sheet (separate from Income Statement categories). */
    public enum BSClass { ASSET, CONTRA_ASSET, LIABILITY, EQUITY, OTHER }

    private final Map<String, BSClass> chart; // normalized name -> BSClass
    private final GeneralLedger ledger;       // reuse ledger to get closing balances

    public BalanceSheetService(Map<String, BSClass> chartOfAccounts, GeneralLedger ledger) {
        this.chart = new HashMap<>();
        if (chartOfAccounts != null) {
            for (var e : chartOfAccounts.entrySet()) {
                this.chart.put(normalize(e.getKey()), e.getValue());
            }
        }
        this.ledger = ledger;

        // Default mapping if account names are missing from user chart
        addDefaultIfMissing("cash", BSClass.ASSET);
        addDefaultIfMissing("bank", BSClass.ASSET);
        addDefaultIfMissing("accounts receivable", BSClass.ASSET);
        addDefaultIfMissing("inventory", BSClass.ASSET);
        addDefaultIfMissing("prepaid expense", BSClass.ASSET);
        addDefaultIfMissing("equipment", BSClass.ASSET);
        addDefaultIfMissing("accumulated depreciation", BSClass.CONTRA_ASSET);

        addDefaultIfMissing("accounts payable", BSClass.LIABILITY);
        addDefaultIfMissing("notes payable", BSClass.LIABILITY);
        addDefaultIfMissing("taxes payable", BSClass.LIABILITY);
        addDefaultIfMissing("wages payable", BSClass.LIABILITY);

        addDefaultIfMissing("owner's equity", BSClass.EQUITY);
        addDefaultIfMissing("capital", BSClass.EQUITY);
        addDefaultIfMissing("common stock", BSClass.EQUITY);
        addDefaultIfMissing("retained earnings", BSClass.EQUITY);
    }

    private void addDefaultIfMissing(String name, BSClass cls) {
        chart.putIfAbsent(normalize(name), cls);
    }

    /** Build a Balance Sheet as of a given date, including Net Income from the Income Statement. */
    public BalanceSheet generate(List<JournalEntry> allEntries, LocalDate asOf, IncomeStatement incomeStatement) {
        if (asOf == null) throw new IllegalArgumentException("As-of date is required.");
        if (allEntries == null) allEntries = Collections.emptyList();

        // Filter only approved entries up to the given date
        List<JournalEntry> filtered = new ArrayList<>();
        for (var entry : allEntries) {
            if (entry == null || entry.getDate() == null) continue;
            if (!"approved".equalsIgnoreCase(entry.getStatus())) continue;
            if (!entry.getDate().isAfter(asOf)) filtered.add(entry);
        }

        ledger.clear();
        ledger.build(filtered);

        Map<String, BigDecimal> assets = new TreeMap<>();
        Map<String, BigDecimal> liabilities = new TreeMap<>();
        Map<String, BigDecimal> equity = new TreeMap<>();

        // Classify each account from ledger balances
        for (String account : ledger.getAccounts()) {
            BigDecimal balance = ledger.getFinalBalance(account).setScale(2, RoundingMode.HALF_UP);
            if (balance.compareTo(BigDecimal.ZERO) == 0) continue;

            BSClass type = chart.getOrDefault(normalize(account), inferBSClass(account));

            switch (type) {
                case ASSET -> merge(assets, account, balance);
                case CONTRA_ASSET -> merge(assets, account, balance.negate());
                case LIABILITY -> merge(liabilities, account, balance.abs());
                case EQUITY -> merge(equity, account, balance.abs());
                default -> { /* Ignore OTHER */ }
            }
        }

        // Add Net Income from Income Statement into Equity section
        if (incomeStatement != null) {
            merge(equity, "Retained Earnings / Net Income", incomeStatement.getNetIncome());
        }

        BigDecimal totalAssets = sum(assets.values());
        BigDecimal totalLiabilities = sum(liabilities.values());
        BigDecimal totalEquity = sum(equity.values());

        return new BalanceSheet(
                asOf,
                assets,
                liabilities,
                equity,
                totalAssets.setScale(2, RoundingMode.HALF_UP),
                totalLiabilities.setScale(2, RoundingMode.HALF_UP),
                totalEquity.setScale(2, RoundingMode.HALF_UP)
        );
    }

    /* ---------------- Helpers ---------------- */

    private static void merge(Map<String, BigDecimal> map, String key, BigDecimal value) {
        map.merge(key.trim(), value, BigDecimal::add);
    }

    private static BigDecimal sum(Collection<BigDecimal> values) {
        BigDecimal total = BigDecimal.ZERO;
        for (BigDecimal v : values) total = total.add(v);
        return total;
    }

    private static String normalize(String s) {
        if (s == null) return "";
        String t = s.trim().toLowerCase();
        t = t.replaceAll("\\s+", " ");
        t = t.replace('–', '-');
        return t;
    }

    /** Infer classification from account name if not found in chart. */
    private static BSClass inferBSClass(String name) {
        String n = normalize(name);
        if (n.contains("accumulated depreciation")) return BSClass.CONTRA_ASSET;
        if (n.endsWith("receivable") || n.contains("cash") || n.contains("bank") ||
                n.contains("inventory") || n.contains("prepaid") || n.contains("equipment") || n.contains("asset"))
            return BSClass.ASSET;
        if (n.endsWith("payable") || n.contains("liability") || n.contains("loan") || n.contains("debt"))
            return BSClass.LIABILITY;
        if (n.contains("equity") || n.contains("capital") || n.contains("stock") || n.contains("retained"))
            return BSClass.EQUITY;
        return BSClass.OTHER;
    }
}
