package model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class BalanceSheet {

    private final LocalDate asOf;
    private final Map<String, BigDecimal> assets;
    private final Map<String, BigDecimal> liabilities;
    private final Map<String, BigDecimal> equity;
    private final BigDecimal totalAssets;
    private final BigDecimal totalLiabilities;
    private final BigDecimal totalEquity;

    public BalanceSheet(
            LocalDate asOf,
            Map<String, BigDecimal> assets,
            Map<String, BigDecimal> liabilities,
            Map<String, BigDecimal> equity,
            BigDecimal totalAssets,
            BigDecimal totalLiabilities,
            BigDecimal totalEquity
    ) {
        this.asOf = asOf;
        this.assets = Collections.unmodifiableMap(new LinkedHashMap<>(assets));
        this.liabilities = Collections.unmodifiableMap(new LinkedHashMap<>(liabilities));
        this.equity = Collections.unmodifiableMap(new LinkedHashMap<>(equity));
        this.totalAssets = totalAssets;
        this.totalLiabilities = totalLiabilities;
        this.totalEquity = totalEquity;
    }

    public LocalDate getAsOf() { return asOf; }
    public Map<String, BigDecimal> getAssets() { return assets; }
    public Map<String, BigDecimal> getLiabilities() { return liabilities; }
    public Map<String, BigDecimal> getEquity() { return equity; }
    public BigDecimal getTotalAssets() { return totalAssets; }
    public BigDecimal getTotalLiabilities() { return totalLiabilities; }
    public BigDecimal getTotalEquity() { return totalEquity; }
}
