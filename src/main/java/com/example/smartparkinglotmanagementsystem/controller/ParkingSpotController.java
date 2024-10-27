package com.example.smartparkinglotmanagementsystem.controller;

import com.example.smartparkinglotmanagementsystem.dto.ParkingSpotDto;
import com.example.smartparkinglotmanagementsystem.dto.Response;
import com.example.smartparkinglotmanagementsystem.service.ParkingSpotService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parkingSpot")
@AllArgsConstructor
public class ParkingSpotController {

    private ParkingSpotService parkingSpotService;


    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteParkingSpot(@PathVariable Long id){
        return new ResponseEntity<>(parkingSpotService.deleteParkingSpot(id), HttpStatus.OK);
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> addParkingSpot(@RequestBody ParkingSpotDto parkingSpotDto){
        return new ResponseEntity<>(parkingSpotService.addParkingSpot(parkingSpotDto), HttpStatus.CREATED);
    }

}
