package model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class IncomeStatement {

    private final LocalDate from;
    private final LocalDate to;
    private final Map<String, BigDecimal> revenues;    // alphabetical order ready
    private final Map<String, BigDecimal> expenses;    // alphabetical order ready
    private final BigDecimal totalRevenues;            // net (after contra)
    private final BigDecimal totalExpenses;
    private final BigDecimal netIncome;
    private final List<String> ignoredAccounts;        // non Rev/Exp accounts seen

    public IncomeStatement(
            LocalDate from,
            LocalDate to,
            Map<String, BigDecimal> revenues,
            Map<String, BigDecimal> expenses,
            BigDecimal totalRevenues,
            BigDecimal totalExpenses,
            BigDecimal netIncome,
            List<String> ignoredAccounts
    ) {
        this.from = from;
        this.to = to;
        this.revenues = Collections.unmodifiableMap(new LinkedHashMap<>(revenues));
        this.expenses = Collections.unmodifiableMap(new LinkedHashMap<>(expenses));
        this.totalRevenues = totalRevenues;
        this.totalExpenses = totalExpenses;
        this.netIncome = netIncome;
        this.ignoredAccounts = List.copyOf(ignoredAccounts);
    }

    public LocalDate getFrom() {
        return from;
    }

    public LocalDate getTo() {
        return to;
    }

    public Map<String, BigDecimal> getRevenues() {
        return revenues;
    }

    public Map<String, BigDecimal> getExpenses() {
        return expenses;
    }

    public BigDecimal getTotalRevenues() {
        return totalRevenues;
    }

    public BigDecimal getTotalExpenses() {
        return totalExpenses;
    }

    public BigDecimal getNetIncome() {
        return netIncome;
    }

    public List<String> getIgnoredAccounts() {
        return ignoredAccounts;
    }
}
