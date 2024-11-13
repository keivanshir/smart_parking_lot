package com.example.smartparkinglotmanagementsystem.mapper;

import com.example.smartparkinglotmanagementsystem.dto.ParkingSpotDto;
import com.example.smartparkinglotmanagementsystem.dto.RevenueDto;
import com.example.smartparkinglotmanagementsystem.dto.VehicleDto;
import com.example.smartparkinglotmanagementsystem.entity.*;
import org.springframework.stereotype.Component;
import com.example.smartparkinglotmanagementsystem.dto.UserDto;


@Component
public class EntityDtoMapper {

    public UserDto mapUserToDtoBasic(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRole().name());
        userDto.setName(user.getName());

        return userDto;
    }



    public ParkingSpotDto mapParkingSpotToDtoBasic(ParkingSpot parkingSpot){
        ParkingSpotDto parkingSpotDto = new ParkingSpotDto();
        parkingSpotDto.setId(parkingSpot.getId());
        parkingSpotDto.setSpotId(parkingSpot.getSpotId());
        parkingSpotDto.setIsOccupied(parkingSpot.getIsOccupied());
        parkingSpotDto.setSpotSize(parkingSpot.getSpotSize());

        return parkingSpotDto;
    }

    public ParkingSpotDto mapParkingSpotToDtoWithVehicle(ParkingSpot parkingSpot){
        ParkingSpotDto parkingSpotDto = mapParkingSpotToDtoBasic(parkingSpot);
        if (parkingSpot.getCurrentVehicle() != null){
            VehicleDto vehicleDto = mapToVehicleDtoBasic(parkingSpot.getCurrentVehicle());
            parkingSpotDto.setCurrentVehicle(vehicleDto);
        }
        return parkingSpotDto;
    }

    public VehicleDto mapToVehicleDtoBasic(Vehicle vehicle){
        VehicleDto vehicleDto = new VehicleDto();
        vehicleDto.setId(vehicle.getId());
        vehicleDto.setVehicleType(vehicle.getVehicleType());
        vehicleDto.setEntryTime(vehicle.getEntryTime());
        vehicleDto.setExitTime(vehicle.getExitTime());
        vehicleDto.setLicensePlate(vehicle.getLicensePlate());
        return vehicleDto;
    }

    public VehicleDto mapToVehicleDtoWithParkingSpot(Vehicle vehicle){
        VehicleDto vehicleDto = mapToVehicleDtoBasic(vehicle);
        if (vehicle.getAssignedSpot() != null){
            ParkingSpotDto parkingSpotDto = mapParkingSpotToDtoBasic(vehicle.getAssignedSpot());
            vehicleDto.setAssignedSpot(parkingSpotDto);
        }
        return vehicleDto;
    }



    public RevenueDto mapToRevenueDto(Revenue revenue){
        RevenueDto revenueDto = new RevenueDto();
        revenueDto.setFee(revenue.getFee());
        revenueDto.setId(revenue.getId());
        if (revenue.getVehicle() != null){
            revenueDto.setVehicle(mapToVehicleDtoBasic(revenue.getVehicle()));
        }
        if (revenue.getAssignedSpot() != null){
            revenueDto.setAssignedSpot(mapParkingSpotToDtoBasic(revenue.getAssignedSpot()));
        }
        return revenueDto;
    }
}
