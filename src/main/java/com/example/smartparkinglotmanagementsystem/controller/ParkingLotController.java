package com.example.smartparkinglotmanagementsystem.controller;

import com.example.smartparkinglotmanagementsystem.dto.Response;
import com.example.smartparkinglotmanagementsystem.dto.VehicleDto;
import com.example.smartparkinglotmanagementsystem.service.implementation.ParkingLotServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parkingLot")
@RequiredArgsConstructor
public class ParkingLotController {

    private final ParkingLotServiceImpl parkingLotService;

    @PostMapping("/registerVehicle")
    public ResponseEntity<Response> registerVehicle(@RequestBody VehicleDto vehicleDto){
        return new ResponseEntity<>(parkingLotService.registerVehicle(vehicleDto), HttpStatus.CREATED);
    }

    @PostMapping("/registerVehicleExit/{id}")
    public ResponseEntity<Response> registerVehicleExit(@PathVariable Long id){
        return new ResponseEntity<>(parkingLotService.registerVehicleExit(id), HttpStatus.OK);
    }


}
