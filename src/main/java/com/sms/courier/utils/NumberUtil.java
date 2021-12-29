package com.sms.courier.utils;

public class NumberUtil {

    private NumberUtil() {

    }

    public static double getPercentage(double divisor, double dividend) {
        double percentage = 0.00;
        if (divisor > 0) {
            percentage = Double.parseDouble(String.format("%.2f", (divisor / dividend)));

        }
        return percentage;
    }

}
