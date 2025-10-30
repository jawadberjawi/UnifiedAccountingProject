package services;

import model.BalanceSheet;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

public class BalanceSheetRenderer {

    private static final int WIDTH = 16;

    public static void render(BalanceSheet bs) {
        System.out.println();
        System.out.println("BALANCE SHEET (As of " + bs.getAsOf() + ")");
        System.out.println("==================================================");

        printSection("ASSETS", bs.getAssets(), bs.getTotalAssets());
        printSection("LIABILITIES", bs.getLiabilities(), bs.getTotalLiabilities());
        printSection("EQUITY", bs.getEquity(), bs.getTotalEquity());

        System.out.println("--------------------------------------------------");
        System.out.println(padRight("Total Assets", 30) + padLeft(fmt(bs.getTotalAssets()), WIDTH));
        System.out.println(padRight("Total Liabilities + Equity", 30)
                + padLeft(fmt(bs.getTotalLiabilities().add(bs.getTotalEquity())), WIDTH));

        boolean balanced = bs.getTotalAssets().compareTo(bs.getTotalLiabilities().add(bs.getTotalEquity())) == 0;
        System.out.println("--------------------------------------------------");
        System.out.println(balanced ? "✅ Balanced" : "❌ Not Balanced");
        System.out.println("==================================================");
    }

    private static void printSection(String title, Map<String, BigDecimal> map, BigDecimal total) {
        System.out.println(title);
        if (map.isEmpty()) {
            System.out.println("  (none)");
        } else {
            for (var e : map.entrySet()) {
                System.out.println("  " + padRight(e.getKey(), 30) + padLeft(fmt(e.getValue()), WIDTH));
            }
        }
        System.out.println("  " + padRight("Total " + title, 30) + padLeft(fmt(total), WIDTH));
        System.out.println();
    }

    /* ---------------- Formatting helpers ---------------- */

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
