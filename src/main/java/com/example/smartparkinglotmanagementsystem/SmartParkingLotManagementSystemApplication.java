package com.example.smartparkinglotmanagementsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@SpringBootApplication
@EnableCaching
public class SmartParkingLotManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartParkingLotManagementSystemApplication.class, args);
    }

}
