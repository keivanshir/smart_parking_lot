package com.example.smartparkinglotmanagementsystem.controller;

import com.example.smartparkinglotmanagementsystem.dto.Response;
import com.example.smartparkinglotmanagementsystem.dto.VehicleDto;
import com.example.smartparkinglotmanagementsystem.service.implementation.ParkingLotServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(summary = "register vehicle entry and assign parking spot and notify spot status",
            responses = {
                    @ApiResponse(responseCode = "201", description = "vehicle saved successfully and parking spot assigned"),
                    @ApiResponse(responseCode = "404", description = "parking spot not found")
            }
    )
    public ResponseEntity<Response> registerVehicle(@RequestBody VehicleDto vehicleDto){
        return new ResponseEntity<>(parkingLotService.registerVehicle(vehicleDto), HttpStatus.CREATED);
    }

    @PostMapping("/registerVehicleExit/{id}")
    @Operation(summary = "register vehicle exit, calculate fee, free up spot and notify spot status",
            responses = {
                    @ApiResponse(responseCode = "200", description = "vehicle exit saved successfully"),
                    @ApiResponse(responseCode = "404", description = "Parking spot or vehicle not found")
            }
    )
    public ResponseEntity<Response> registerVehicleExit(@PathVariable Long id){
        return new ResponseEntity<>(parkingLotService.registerVehicleExit(id), HttpStatus.OK);
    }


}
