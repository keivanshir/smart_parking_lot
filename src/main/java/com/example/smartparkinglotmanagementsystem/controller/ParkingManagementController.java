package com.example.smartparkinglotmanagementsystem.controller;

import com.example.smartparkinglotmanagementsystem.dto.Response;
import com.example.smartparkinglotmanagementsystem.service.ParkingManagementService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/management")
@AllArgsConstructor
public class ParkingManagementController {

    private final ParkingManagementService parkingManagementService;

    @GetMapping("/parkingSpotsStat")
    public ResponseEntity<Response> viewRealtimeStatus(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue= "1000") int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        return new ResponseEntity<>(parkingManagementService.viewRealtimeStatus(pageable), HttpStatus.OK);
    }

    @GetMapping("/occupiedParkingSpots")
    public ResponseEntity<Response> viewOccupancyRate(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue= "1000") int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        return new ResponseEntity<>(parkingManagementService.viewOccupancyRate(pageable), HttpStatus.OK);
    }
}
