package com.cloud.kitchen.util;

import java.text.DecimalFormat;
import java.util.List;

import static com.cloud.kitchen.constants.StringConstants.DECIMAL_PRECISION;

/**
 * The Utility class provides various utility methods for common operations,
 * including conversions, averaging, decimal precision formatting, and current timestamp retrieval.
 */
public class Utility {

    /**
     * Converts milliseconds to minutes.
     *
     * @param value Milliseconds value to convert.
     * @return Equivalent value in minutes.
     */
    public static double convertToMinutes(long value) {
        return (double) value / 60000;
    }

    /**
     * Calculates the average of a list of Double values.
     *
     * @param list List of Double values.
     * @return Average value of the list.
     */
    public static double averageOfList(List<Double> list){
        return list.stream()
                .mapToDouble(Number::doubleValue)
                .average()
                .orElse(0.0);
    }

    /**
     * Calculates the average of a list of Double values and formats it with decimal precision.
     *
     * @param list List of Double values.
     * @return Average value formatted with decimal precision.
     */
    public static String average(List<Double> list){
        return decimalPrecision(averageOfList(list));
    }

    /**
     * Formats a double value to a specified decimal precision.
     *
     * @param value Double value to format.
     * @return Formatted string with specified decimal precision.
     */
    public static String decimalPrecision(double value){
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(DECIMAL_PRECISION);
        return df.format(value);
    }

    /**
     * Retrieves the current timestamp in milliseconds.
     *
     * @return Current timestamp in milliseconds.
     */
    public static long currentMilliSeconds() {
        return System.currentTimeMillis();
    }
}
