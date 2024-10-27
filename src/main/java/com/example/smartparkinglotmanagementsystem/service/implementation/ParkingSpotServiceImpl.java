package com.example.smartparkinglotmanagementsystem.service.implementation;

import com.example.smartparkinglotmanagementsystem.dto.ParkingSpotDto;
import com.example.smartparkinglotmanagementsystem.dto.Response;
import com.example.smartparkinglotmanagementsystem.entity.ParkingSpot;
import com.example.smartparkinglotmanagementsystem.exception.NotFoundException;
import com.example.smartparkinglotmanagementsystem.mapper.EntityDtoMapper;
import com.example.smartparkinglotmanagementsystem.repository.ParkingSpotRepository;
import com.example.smartparkinglotmanagementsystem.service.ParkingSpotService;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class ParkingSpotServiceImpl implements ParkingSpotService {
    private ParkingSpotRepository parkingSpotRepository;
    private EntityDtoMapper entityDtoMapper;

    @Override
    public Response freeUpParkingSpot(Long id) {
        ParkingSpot parkingSpot = parkingSpotRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Parking spot not found for id: " + id));

        parkingSpot.setIsOccupied(false);
        parkingSpot.setCurrentVehicle(null);
        parkingSpotRepository.save(parkingSpot);
        log.info("parking spot " + parkingSpot.getSpotId() + " freed");

        return Response.builder()
                .status(200)
                .message("Parking spot " + parkingSpot.getSpotId() + " freed up successfully")
                .build();
    }

    @Override
    public Response addParkingSpot(ParkingSpotDto parkingSpotDto) {
        ParkingSpot parkingSpot = new ParkingSpot();
        parkingSpot.setSpotId(parkingSpotDto.getSpotId());
        parkingSpot.setSpotSize(parkingSpotDto.getSpotSize());

        parkingSpotRepository.save(parkingSpot);

        log.info("parking spot added successfully");

        return Response.builder()
                .parkingSpot(entityDtoMapper.mapParkingSpotToDtoBasic(parkingSpot))
                .build();
    }

    @Override
    public Response deleteParkingSpot(Long id) {
        ParkingSpot parkingSpot = parkingSpotRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Parking spot not found for id: " + id));
        parkingSpotRepository.delete(parkingSpot);
        log.info("parking spot deleted successfully");

        return Response.builder()
                .status(200)
                .message("Parking spot " + parkingSpot.getSpotId() + " deleted successfully")
                .build();
    }


}
