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
import com.example.smartparkinglotmanagementsystem.specification.RevenueSpecification;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@AllArgsConstructor
@Slf4j
public class ReportingServiceImpl implements ReportingService {

    private final VehicleRepository vehicleRepository;
    private final EntityDtoMapper entityDtoMapper;
    private final RevenueRepository revenueRepository;

    @Override
    public Response generateReportOfAllParkedVehicles(Pageable pageable) {
//        Specification<Vehicle> spec = Specification.where(VehicleSpecification.isVehicleParked());

        Page<Vehicle> vehiclePage = vehicleRepository.findAll(pageable);

        if (vehiclePage.isEmpty())
            throw new NotFoundException("No Parked vehicle is found");

        log.info("generating report for parked vehicles: ");
        List<VehicleDto> vehicleDtoList = vehiclePage.getContent()
                .stream()
                .filter(vehicle -> vehicle.getAssignedSpot().getIsOccupied()
                        && vehicle.getAssignedSpot().getCurrentVehicle() != null)
                .map(entityDtoMapper::mapToVehicleWithParkingSpot)
                .toList();

        log.info(vehicleDtoList.toString());

        return Response.builder()
                .status(200)
                .message("All Parked vehicles")
                .vehicleDtoList(vehicleDtoList)
                .build();
    }

    @Override
    public Response generateRevenueReport(Frequency frequency, Pageable pageable) {
        List<RevenueDto> revenueDtoList;
        switch (frequency) {
            case DAILY -> {
                log.info("generating report for DAILY revenues");
                revenueDtoList = dailyReport(pageable);
            }
            case WEEKLY -> {
                log.info("generating report for WEEKLY revenues");
                revenueDtoList = weeklyReport(pageable);
            }
            case MONTHLY -> {
                log.info("generating report for MONTHLY revenues");
                revenueDtoList = monthlyReport(pageable);
            }
            default -> revenueDtoList = dailyReport(pageable);
        }
        return Response.builder()
                .status(200)
                .message("Revenues with " + frequency.name() + " frequency")
                .revenueDtoList(revenueDtoList)
                .build();
    }

    private List<RevenueDto> monthlyReport(Pageable pageable) {
        Specification<Revenue> spec = Specification
                .where(RevenueSpecification
                        .createdBetween(LocalDateTime.now().minusMonths(1), LocalDateTime.now()));

        return getRevenueDtos(pageable, spec);
    }

    private List<RevenueDto> weeklyReport(Pageable pageable) {
        Specification<Revenue> spec = Specification
                .where(RevenueSpecification
                        .createdBetween(LocalDateTime.now().minusWeeks(1), LocalDateTime.now()));

        return getRevenueDtos(pageable, spec);
    }

    private List<RevenueDto> dailyReport(Pageable pageable) {
        Specification<Revenue> spec = Specification
                .where(RevenueSpecification
                        .createdBetween(LocalDateTime.now().minusDays(1), LocalDateTime.now()));

        return getRevenueDtos(pageable, spec);
    }

    private List<RevenueDto> getRevenueDtos(Pageable pageable, Specification<Revenue> spec) {
        Page<Revenue> revenuePage = revenueRepository.findAll(spec, pageable);

        if (revenuePage.isEmpty())
            throw new NotFoundException("Revenues not found");

        List<RevenueDto> revenueDtoList = revenuePage.getContent()
                .stream()
                .map(entityDtoMapper::mapToRevenueDto)
                .toList();

        log.info("revenues: " + revenueDtoList);

        return revenueDtoList;
    }
}
