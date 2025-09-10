package services;

import model.IncomeStatement;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

public class IncomeStatementRenderer {
    private static final int WIDTH = 14;

    public static void render(IncomeStatement is) {
        System.out.println();
        System.out.println("Income Statement");
        System.out.println("For the period: " + is.getFrom() + " to " + is.getTo());
        System.out.println("==================================================");

        // Revenues
        System.out.println("Revenues");
        if (is.getRevenues().isEmpty()) {
            System.out.println(" (none)");
        } else {
            for (Map.Entry<String, BigDecimal> e : is.getRevenues().entrySet()) {
                System.out.println(" " + padRight(e.getKey(), 30) + padLeft(fmt(e.getValue()), WIDTH));
            }
        }
        System.out.println(" " + padRight("Total Revenues", 30) + padLeft(fmt(is.getTotalRevenues()), WIDTH));
        System.out.println();

        // Expenses
        System.out.println("Expenses");
        if (is.getExpenses().isEmpty()) {
            System.out.println(" (none)");
        } else {
            for (Map.Entry<String, BigDecimal> e : is.getExpenses().entrySet()) {
                System.out.println(" " + padRight(e.getKey(), 30) + padLeft(fmt(e.getValue()), WIDTH));
            }
        }
        System.out.println(" " + padRight("Total Expenses", 30) + padLeft(fmt(is.getTotalExpenses()), WIDTH));
        System.out.println();

        // Net Income
        System.out.println(padRight("Net Income", 32) + padLeft(fmt(is.getNetIncome()), WIDTH));
        System.out.println("==================================================");

        if (!is.getIgnoredAccounts().isEmpty()) {
            System.out.println("Note: Ignored (not revenue/expense): " + String.join(", ", is.getIgnoredAccounts()));
        }
    }

    private static String fmt(BigDecimal v) {
        return v.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    private static String padLeft(String s, int width) {
        if (s == null) s = "";
        if (s.length() >= width) return s;
        return " ".repeat(width - s.length()) + s;
    }

    private static String padRight(String s, int width) {
        if (s == null) s = "";
        if (s.length() >= width) return s;
        return s + " ".repeat(width - s.length());
    }
}
