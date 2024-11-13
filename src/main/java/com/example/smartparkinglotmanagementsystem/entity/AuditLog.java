package com.example.smartparkinglotmanagementsystem.entity;

import com.example.smartparkinglotmanagementsystem.security.AuthUser;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Getter
@Setter
@Document(collection = "AuditLog")
public class AuditLog {


    private String action;

    @CreatedBy
    private AuthUser createdBy;

    @CreatedDate
    private LocalDateTime createdAt;
}
