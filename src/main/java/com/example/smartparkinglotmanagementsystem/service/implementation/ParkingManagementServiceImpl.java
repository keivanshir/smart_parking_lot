package com.example.smartparkinglotmanagementsystem.service.implementation;

import com.example.smartparkinglotmanagementsystem.dto.ParkingSpotDto;
import com.example.smartparkinglotmanagementsystem.dto.Response;
import com.example.smartparkinglotmanagementsystem.entity.ParkingSpot;
import com.example.smartparkinglotmanagementsystem.exception.NotFoundException;
import com.example.smartparkinglotmanagementsystem.mapper.EntityDtoMapper;
import com.example.smartparkinglotmanagementsystem.repository.ParkingSpotRepository;
import com.example.smartparkinglotmanagementsystem.service.ParkingManagementService;
import com.example.smartparkinglotmanagementsystem.specification.ManagementSpecification;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ParkingManagementServiceImpl implements ParkingManagementService {

    private ParkingSpotRepository parkingSpotRepository;
    private EntityDtoMapper entityDtoMapper;

    @Override
    @Cacheable(cacheNames = "getAllParkingSpots", key = "'allParkingSpots'")
    public List<ParkingSpotDto> viewRealtimeStatus(Page<ParkingSpot> parkingSpotPage) {

        List<ParkingSpotDto> parkingSpotDtoList = parkingSpotPage.getContent()
                .stream()
                .map(entityDtoMapper::mapParkingSpotToDtoWithVehicle)
                .collect(Collectors.toList());

        log.info("viewing all parking spots: " + parkingSpotDtoList);

        return parkingSpotDtoList;
    }

    @Override
    public Response viewOccupancyRate(Pageable pageable) {
        Page<ParkingSpot> parkingSpotPage = parkingSpotRepository.findAll(ManagementSpecification.isOccupiedSpot(),pageable);

        if (parkingSpotPage.isEmpty())
            throw new NotFoundException("All Parking spots are vacant");

        List<ParkingSpotDto> parkingSpotDtoList = parkingSpotPage
                .getContent()
                .stream()
                .map(entityDtoMapper::mapParkingSpotToDtoWithVehicle)
                .collect(Collectors.toList());

        log.info("viewing occupied parking spots: " + parkingSpotDtoList);

        return Response.builder()
                .status(200)
                .message("Occupied parking spots")
                .parkingSpotDtoList(parkingSpotDtoList)
                .totalElement(parkingSpotPage.getTotalElements())
                .totalPage(parkingSpotPage.getTotalPages())
                .build();
    }
}
