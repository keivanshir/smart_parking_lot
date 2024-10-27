package com.example.smartparkinglotmanagementsystem.strategy;

import java.time.LocalDateTime;

public class CarFeeStrategy implements FeeCalculationStrategy{
    private static final int RATE = 20000;
    @Override
    public long calculateFee(LocalDateTime entryTime, LocalDateTime exitTime) {
        return FeeCalculationUtils.calculateBaseFee(entryTime, exitTime, RATE);
    }
}
