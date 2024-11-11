package com.example.smartparkinglotmanagementsystem.service.implementation;

import com.example.smartparkinglotmanagementsystem.dto.ParkingSpotDto;
import com.example.smartparkinglotmanagementsystem.dto.Response;
import com.example.smartparkinglotmanagementsystem.entity.ParkingSpot;
import com.example.smartparkinglotmanagementsystem.enums.SpotStatus;
import com.example.smartparkinglotmanagementsystem.exception.NotFoundException;
import com.example.smartparkinglotmanagementsystem.mapper.EntityDtoMapper;
import com.example.smartparkinglotmanagementsystem.repository.ParkingSpotRepository;
import com.example.smartparkinglotmanagementsystem.service.ParkingSpotService;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class ParkingSpotServiceImpl implements ParkingSpotService {
    private ParkingSpotRepository parkingSpotRepository;
    private EntityDtoMapper entityDtoMapper;
    private ParkingWebsocketServiceImpl parkingWebSocketService;


    @Override
    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "parkingSpot", key = "#id"),
                    @CacheEvict(cacheNames = "getAllParkingSpots", key = "'allParkingSpots'")
            },
            put = @CachePut(cacheNames = "parkingSpot", key = "#id")
    )
    public ParkingSpotDto freeUpParkingSpot(Long id) {
        ParkingSpot parkingSpot = parkingSpotRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Parking spot not found for id: " + id));

        parkingSpot.setIsOccupied(false);
        parkingSpot.setCurrentVehicle(null);
        ParkingSpot savedParkingSpot = parkingSpotRepository.save(parkingSpot);
        log.info("parking spot " + savedParkingSpot.getSpotId() + " freed");

        // web socket to notify all connected clients
        ParkingSpotDto dtoForNotify = entityDtoMapper.mapParkingSpotToDtoBasic(savedParkingSpot);
        dtoForNotify.setSpotStatus(SpotStatus.EMPTY);
        parkingWebSocketService.sendOccupancyUpdate(dtoForNotify);
        log.info("connected web socket notified");

        return dtoForNotify;
    }

    @Override
    @CacheEvict(cacheNames = "getAllParkingSpots", key = "'allParkingSpots'")
    public ParkingSpotDto addParkingSpot(ParkingSpotDto parkingSpotDto) {
        ParkingSpot parkingSpot = new ParkingSpot();
        parkingSpot.setSpotId(parkingSpotDto.getSpotId());
        parkingSpot.setSpotSize(parkingSpotDto.getSpotSize());

        ParkingSpot savedSpot = parkingSpotRepository.save(parkingSpot);
        log.info("parking spot added successfully");

        ParkingSpotDto dtoForNotify = entityDtoMapper.mapParkingSpotToDtoBasic(savedSpot);
        dtoForNotify.setSpotStatus(SpotStatus.EMPTY);
        parkingWebSocketService.sendOccupancyUpdate(dtoForNotify);
        log.info("connected web socket notified");

        return entityDtoMapper.mapParkingSpotToDtoBasic(savedSpot);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "getAllParkingSpots", key = "'allParkingSpots'")
    })
    public Response deleteParkingSpot(Long id) {
        ParkingSpot parkingSpot = parkingSpotRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Parking spot not found for id: " + id));
        parkingSpotRepository.delete(parkingSpot);
        log.info("parking spot deleted successfully");

        ParkingSpotDto dtoForNotify = entityDtoMapper.mapParkingSpotToDtoBasic(parkingSpot);
        dtoForNotify.setSpotStatus(SpotStatus.REMOVED);
        parkingWebSocketService.sendOccupancyUpdate(dtoForNotify);
        log.info("connected web socket notified");

        return Response.builder()
                .status(200)
                .message("Parking spot " + parkingSpot.getSpotId() + " deleted successfully")
                .build();
    }


}
