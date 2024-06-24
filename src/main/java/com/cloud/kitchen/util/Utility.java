package com.cloud.kitchen.util;

import java.util.List;

public class Utility {

    public static double convertToMinutes(long time){
        return (double) time/ 60000;
    }

    public static double average(List<Double> list){
        return list.stream()
                .mapToDouble(Number::doubleValue)
                .average()
                .orElse(0.0);
    }
}
