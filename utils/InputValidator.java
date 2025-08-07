package utils;

import java.time.LocalDate;

public class InputValidator {

    public static boolean isValidAccount(String account) {
        return account != null && !account.trim().isEmpty();
    }

    public static boolean isValidAmount(double amount) {
        return amount > 0;
    }

    public static boolean isValidDate(LocalDate date) {
        return date != null && !date.isAfter(LocalDate.now());
    }

    public static boolean isValidStatus(String status) {
        if (status == null) return false;

        status = status.trim().toLowerCase();
        return status.equals("approved") ||
                status.equals("pending") ||
                status.equals("rejected");
    }
}
