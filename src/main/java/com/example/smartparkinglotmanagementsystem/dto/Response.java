package com.example.smartparkinglotmanagementsystem.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
// to ignore a property which doesn't have a value
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class Response {
    private int status;
    private String message;
    @JsonIgnore
    private final LocalDateTime timestamp = LocalDateTime.now();

    private String token;
    private String role;
    private String expirationTime;

    // for pagination
    private int totalPage;
    private long totalElement;

    private UserDto user;
    private ParkingSpotDto parkingSpot;
    private RevenueDto revenueDto;
    private List<ParkingSpotDto> parkingSpotDtoList;
    private List<VehicleDto> vehicleDtoList;
    private List<RevenueDto> revenueDtoList;
}
