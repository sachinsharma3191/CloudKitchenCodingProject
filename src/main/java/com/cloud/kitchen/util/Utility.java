package com.cloud.kitchen.util;

import java.util.List;
import java.text.DecimalFormat;

import static com.cloud.kitchen.constants.StringConstants.DECIMAL_PRECISION;

public class Utility {

    public static double convertToMinutes(long time){
        return (double) time/ 60000;
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
}
