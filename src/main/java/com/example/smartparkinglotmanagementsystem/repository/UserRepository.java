package com.example.smartparkinglotmanagementsystem.repository;

import com.example.smartparkinglotmanagementsystem.dto.LoginRequest;
import com.example.smartparkinglotmanagementsystem.dto.Response;
import com.example.smartparkinglotmanagementsystem.dto.UserDto;
import com.example.smartparkinglotmanagementsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

}
