package com.example.smartparkinglotmanagementsystem.service.implementation;

import com.example.smartparkinglotmanagementsystem.dto.Response;
import com.example.smartparkinglotmanagementsystem.dto.RevenueDto;
import com.example.smartparkinglotmanagementsystem.dto.VehicleDto;
import com.example.smartparkinglotmanagementsystem.entity.Revenue;
import com.example.smartparkinglotmanagementsystem.entity.Vehicle;
import com.example.smartparkinglotmanagementsystem.enums.Frequency;
import com.example.smartparkinglotmanagementsystem.exception.NotFoundException;
import com.example.smartparkinglotmanagementsystem.mapper.EntityDtoMapper;
import com.example.smartparkinglotmanagementsystem.repository.RevenueRepository;
import com.example.smartparkinglotmanagementsystem.repository.VehicleRepository;
import com.example.smartparkinglotmanagementsystem.service.ReportingService;
import com.example.smartparkinglotmanagementsystem.specification.ReportingSpecification;
import com.example.smartparkinglotmanagementsystem.specification.RevenueSpecification;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@AllArgsConstructor
@Slf4j
public class ReportingServiceImpl implements ReportingService {

    private final VehicleRepository vehicleRepository;
    private final EntityDtoMapper entityDtoMapper;
    private final RevenueRepository revenueRepository;

    @Override
    @Transactional
    public Response generateReportOfAllParkedVehicles(Pageable pageable) {
        Page<Vehicle> vehiclePage = vehicleRepository.findAll(ReportingSpecification.parkedCars(), pageable);

        if (vehiclePage.isEmpty())
            throw new NotFoundException("No Parked vehicle is found");

        log.info("generating report for parked vehicles: ");
        List<VehicleDto> vehicleDtoList = vehiclePage.getContent()
                .stream()
                .map(entityDtoMapper::mapToVehicleDtoWithParkingSpot)
                .toList();

        log.info(vehicleDtoList.toString());

        return Response.builder()
                .status(200)
                .message("All Parked vehicles")
                .vehicleDtoList(vehicleDtoList)
                .totalElement(vehiclePage.getTotalElements())
                .totalPage(vehiclePage.getTotalPages())
                .build();
    }

    @Override
    @Transactional
    public Response generateRevenueReport(Frequency frequency, Pageable pageable) {
        List<RevenueDto> revenueDtoList = new ArrayList<>();

        Response.ResponseBuilder responseBuilder = Response.builder()
                .status(200)
                .message("Revenues with " + frequency.name() + " frequency");

        switch (frequency) {
            case DAILY -> {
                log.info("generating report for DAILY revenues");
                revenueDtoList.addAll(dailyReport(pageable, responseBuilder));
            }
            case WEEKLY -> {
                log.info("generating report for WEEKLY revenues");
                revenueDtoList.addAll(weeklyReport(pageable, responseBuilder));
            }
            case MONTHLY -> {
                log.info("generating report for MONTHLY revenues");
                revenueDtoList.addAll(monthlyReport(pageable, responseBuilder));
            }
            default -> revenueDtoList.addAll(dailyReport(pageable, responseBuilder));
        }
        return responseBuilder
                .revenueDtoList(revenueDtoList)
                .build();
    }

    private List<RevenueDto> monthlyReport(Pageable pageable, Response.ResponseBuilder builder) {
        Specification<Revenue> spec = Specification
                .where(RevenueSpecification
                        .createdBetween(LocalDateTime.now().minusMonths(1), LocalDateTime.now()));

        return getRevenueDtos(pageable, spec, builder);
    }

    private List<RevenueDto> weeklyReport(Pageable pageable,Response.ResponseBuilder builder) {
        Specification<Revenue> spec = Specification
                .where(RevenueSpecification
                        .createdBetween(LocalDateTime.now().minusWeeks(1), LocalDateTime.now()));

        return getRevenueDtos(pageable, spec, builder);
    }

    private List<RevenueDto> dailyReport(Pageable pageable, Response.ResponseBuilder builder) {
        Specification<Revenue> spec = Specification
                .where(RevenueSpecification
                        .createdBetween(LocalDateTime.now().minusDays(1), LocalDateTime.now()));

        return getRevenueDtos(pageable, spec, builder);
    }

    private List<RevenueDto> getRevenueDtos(Pageable pageable, Specification<Revenue> spec, Response.ResponseBuilder builder) {
        Page<Revenue> revenuePage = revenueRepository.findAll(spec, pageable);

        if (revenuePage.isEmpty())
            throw new NotFoundException("Revenues not found");

        List<RevenueDto> revenueDtoList = revenuePage.getContent()
                .stream()
                .map(entityDtoMapper::mapToRevenueDto)
                .toList();

        builder.totalElement(revenuePage.getTotalElements())
                .totalPage(revenuePage.getTotalPages());

        log.info("revenues: " + revenueDtoList);

        return revenueDtoList;
    }
}
