package com.example.smartparkinglotmanagementsystem.strategy;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class FeeCalculationUtils {
    private static final double DISCOUNT_THRESHOLD = 8.0;
    private static final double DISCOUNT_RATE = 0.1;

    public static long calculateBaseFee(LocalDateTime entryTime, LocalDateTime exitTime, int ratePerHour) {
        long hours = Duration.between(entryTime, exitTime).plus(1, ChronoUnit.HOURS).toHours();
        long fee = hours * ratePerHour;
        if (hours > DISCOUNT_THRESHOLD) fee *= (1 - DISCOUNT_RATE);
        return fee;
    }
}
