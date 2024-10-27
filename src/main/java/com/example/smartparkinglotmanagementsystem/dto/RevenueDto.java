package com.example.smartparkinglotmanagementsystem.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RevenueDto {
    private Long id;
    private Long fee;
    private VehicleDto vehicle;
}
