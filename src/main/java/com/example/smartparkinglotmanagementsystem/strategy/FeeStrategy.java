package com.example.smartparkinglotmanagementsystem.strategy;

import com.example.smartparkinglotmanagementsystem.enums.VehicleType;


public class FeeStrategy {
    public static FeeCalculationStrategy getFeeStrategy(VehicleType vehicleType) {
        return switch (vehicleType) {
            case MOTORCYCLE -> new MotorCycleFeeStrategy();
            case CAR -> new CarFeeStrategy();
            case TRUCK -> new TruckFeeStrategy();
        };
    }
}
