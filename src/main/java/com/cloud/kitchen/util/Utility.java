package com.cloud.kitchen.util;

import java.text.DecimalFormat;
import java.util.List;

import static com.cloud.kitchen.constants.StringConstants.DECIMAL_PRECISION;

public class Utility {

    public static double convertToMinutes(long value) {
        return (double) value / 60000;
    }

    public static double averageOfList(List<Double> list){
        return list.stream()
                .mapToDouble(Number::doubleValue)
                .average()
                .orElse(0.0);
    }

    public static String average(List<Double> list){
        return decimalPrecision(averageOfList(list));
    }

    public static String decimalPrecision(double value){
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(DECIMAL_PRECISION);
        return df.format(value);
    }

    public static long currentMilliSeconds() {
        return System.currentTimeMillis();
    }
}
