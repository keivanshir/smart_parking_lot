package com.example.smartparkinglotmanagementsystem.strategy;

import java.time.LocalDateTime;

public class TruckFeeStrategy implements FeeCalculationStrategy{
    private static final int RATE = 30000;

    @Override
    public long calculateFee(LocalDateTime entryTime, LocalDateTime exitTime) {
        return FeeCalculationUtils.calculateBaseFee(entryTime, exitTime, RATE);
    }
}
