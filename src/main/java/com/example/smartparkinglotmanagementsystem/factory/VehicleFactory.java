package com.example.smartparkinglotmanagementsystem.factory;

import com.example.smartparkinglotmanagementsystem.entity.ParkingSpot;
import com.example.smartparkinglotmanagementsystem.entity.Vehicle;
import com.example.smartparkinglotmanagementsystem.enums.VehicleType;
import com.example.smartparkinglotmanagementsystem.enums.SpotSize;

import java.util.List;

public class VehicleFactory {

    public static ParkingSpot assignSpot(Vehicle vehicle, List<ParkingSpot> spots) {
        for (ParkingSpot spot : spots) {
            if (!spot.getIsOccupied() && isSpotSuitable(vehicle.getVehicleType(), spot.getSpotSize())) {
                vehicle.setAssignedSpot(spot);
                spot.setCurrentVehicle(vehicle);
                spot.setIsOccupied(true);
                return spot;
            }
        }
        return null;
    }

    private static boolean isSpotSuitable(VehicleType vehicleType, SpotSize spotSize) {
        return switch (vehicleType) {
            case MOTORCYCLE -> spotSize == SpotSize.SMALL;
            case CAR -> spotSize == SpotSize.MEDIUM;
            case TRUCK -> spotSize == SpotSize.LARGE;
        };
    }
}
