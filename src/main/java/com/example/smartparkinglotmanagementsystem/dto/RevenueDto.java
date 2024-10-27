package com.example.smartparkinglotmanagementsystem.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class RevenueDto {
    private Long id;
    private Long fee;
    private VehicleDto vehicle;
}
