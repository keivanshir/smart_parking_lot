package com.example.smartparkinglotmanagementsystem.service.implementation;

import com.example.smartparkinglotmanagementsystem.dto.ParkingSpotDto;
import com.example.smartparkinglotmanagementsystem.service.ParkingWebSocketService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ParkingWebsocketServiceImpl implements ParkingWebSocketService {

    private SimpMessagingTemplate messagingTemplate;

    public void sendOccupancyUpdate(ParkingSpotDto parkingSpotDto) {
        // Broadcasts the parking spot status to all connected clients
        messagingTemplate.convertAndSend("/topic/occupancy", parkingSpotDto);
    }
}
