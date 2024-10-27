package com.example.smartparkinglotmanagementsystem.strategy;

import java.time.LocalDateTime;

public interface FeeCalculationStrategy {
    long calculateFee(LocalDateTime entryTime, LocalDateTime exitTime);
}
