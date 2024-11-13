package com.example.smartparkinglotmanagementsystem.service.implementation;

import com.example.smartparkinglotmanagementsystem.dto.ParkingSpotDto;
import com.example.smartparkinglotmanagementsystem.dto.Response;
import com.example.smartparkinglotmanagementsystem.dto.VehicleDto;
import com.example.smartparkinglotmanagementsystem.entity.AuditLog;
import com.example.smartparkinglotmanagementsystem.entity.ParkingSpot;
import com.example.smartparkinglotmanagementsystem.entity.Revenue;
import com.example.smartparkinglotmanagementsystem.entity.Vehicle;
import com.example.smartparkinglotmanagementsystem.enums.SpotStatus;
import com.example.smartparkinglotmanagementsystem.exception.NotFoundException;
import com.example.smartparkinglotmanagementsystem.factory.VehicleFactory;
import com.example.smartparkinglotmanagementsystem.mapper.EntityDtoMapper;
import com.example.smartparkinglotmanagementsystem.repository.AuditRepository;
import com.example.smartparkinglotmanagementsystem.repository.ParkingSpotRepository;
import com.example.smartparkinglotmanagementsystem.repository.RevenueRepository;
import com.example.smartparkinglotmanagementsystem.repository.VehicleRepository;
import com.example.smartparkinglotmanagementsystem.service.ParkingLotService;
import com.example.smartparkinglotmanagementsystem.service.ParkingSpotService;
import com.example.smartparkinglotmanagementsystem.strategy.FeeCalculationStrategy;
import com.example.smartparkinglotmanagementsystem.strategy.FeeStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParkingLotServiceImpl implements ParkingLotService {

    private final VehicleRepository vehicleRepository;
    private final ParkingSpotRepository parkingSpotRepository;
    private final ParkingSpotService parkingSpotService;
    private final RevenueRepository revenueRepository;
    private final EntityDtoMapper entityDtoMapper;
    private final ParkingWebsocketServiceImpl parkingWebsocketService;
    private final AuditRepository auditRepository;

    @Override
    @Transactional
    public synchronized Response registerVehicle(VehicleDto vehicleDto) {

        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate(vehicleDto.getLicensePlate());
        vehicle.setVehicleType(vehicleDto.getVehicleType());
        vehicle.setEntryTime(LocalDateTime.now());
        List<ParkingSpot> parkingSpotList = parkingSpotRepository.findAll();

        ParkingSpot parkingSpot = VehicleFactory.assignSpot(vehicle, parkingSpotList);

        String message = parkingSpot != null ? "Parking spot "+ parkingSpot.getSpotId() +
                " has assigned to vehicle "
                + vehicle.getLicensePlate() : "No Parking spot available";
        if (parkingSpot != null)
            vehicleRepository.save(vehicle);
        else
            throw new NotFoundException(message);

        AuditLog audit = new AuditLog();
        audit.setAction("INSERT - Vehicle (vehicle license plate: "+ vehicle.getLicensePlate()
                + ", vehicle type: " + vehicle.getVehicleType()
                + ", entry time: " + vehicle.getEntryTime()
                + ", spot size: " + vehicle.getAssignedSpot().getSpotSize()
        + ")");
        auditRepository.save(audit);

        log.info("Vehicle with " + vehicle.getLicensePlate() + " registered at time " + vehicle.getEntryTime());

        ParkingSpotDto dtoForNotify = entityDtoMapper.mapParkingSpotToDtoWithVehicle(parkingSpot);
        dtoForNotify.setSpotStatus(SpotStatus.FULL);
        parkingWebsocketService.sendOccupancyUpdate(dtoForNotify);
        log.info("connected web socket notified");

        return Response.builder()
                .status(201)
                .message(message)
                .build();
    }


    @Override
    @Transactional
    public synchronized Response registerVehicleExit(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Vehicle not found with id " + id));

        vehicle.setExitTime(LocalDateTime.now());
        vehicle.setAssignedSpot(null);

        FeeCalculationStrategy calculationStrategy = FeeStrategy.getFeeStrategy(vehicle.getVehicleType());
        Long fee = calculationStrategy.calculateFee(vehicle.getEntryTime(), vehicle.getExitTime());

       ParkingSpot parkingSpot = parkingSpotRepository.findParkingSpotByCurrentVehicle(vehicle)
                .orElseThrow(() -> new NotFoundException("ParkingSpot not found with vehicle id " + id));
       Revenue revenue = new Revenue();
        revenue.setVehicle(vehicle);
        revenue.setFee(fee);
        revenue.setAssignedSpot(parkingSpot);
        revenueRepository.save(revenue);
        log.info("revenue made: " + revenue.getFee());

        AuditLog auditRevenue = new AuditLog();
        auditRevenue.setAction("INSERT - REVENUE (vehicle license plate: "
                + revenue.getVehicle().getLicensePlate()
                + ", fee: " + revenue.getFee()
                + ", spot id: " + revenue.getAssignedSpot().getSpotId()
                + ")");
        auditRepository.save(auditRevenue);

        Response freedParkingSpotResponse = Response.builder()
                .status(200)
                .parkingSpot(parkingSpotService.freeUpParkingSpot(parkingSpot.getId()))
                .message("Parking spot " + parkingSpot.getSpotId() + " freed up successfully")
                .build();

        freedParkingSpotResponse.setRevenueDto(entityDtoMapper.mapToRevenueDto(revenue));
        vehicleRepository.save(vehicle);

        log.info("Vehicle with " + vehicle.getLicensePlate() + " exited at time " + vehicle.getExitTime());
        AuditLog auditVehicle = new AuditLog();
        auditVehicle.setAction("UPDATE - Vehicle ("+ vehicle.getId()
                + ", " + vehicle.getLicensePlate()
                + ", " + vehicle.getVehicleType()
                + ", " + vehicle.getEntryTime()
                + ", " + vehicle.getExitTime()
                + ")");
        auditRepository.save(auditVehicle);

        return freedParkingSpotResponse;
    }
}
