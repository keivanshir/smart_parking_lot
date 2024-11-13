package com.example.smartparkinglotmanagementsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.config.EnableMongoAuditing;


@SpringBootApplication
@EnableCaching
@EnableMongoAuditing
public class SmartParkingLotManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartParkingLotManagementSystemApplication.class, args);
    }

}
