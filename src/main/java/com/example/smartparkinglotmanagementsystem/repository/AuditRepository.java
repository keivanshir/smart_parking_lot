package com.example.smartparkinglotmanagementsystem.repository;

import com.example.smartparkinglotmanagementsystem.entity.AuditLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuditRepository extends MongoRepository<AuditLog, Long> {
}
