package com.example.api.controllers;

import com.example.api.dto.AirportDto;
import com.example.api.services.AirportService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("api/v0/airports")
public class AirportController {
    private AirportService airportService;

    @PostMapping
    public ResponseEntity<AirportDto> createAirport(@RequestBody AirportDto airportDto) {
        airportDto = airportService.createAirport(airportDto);
        return new ResponseEntity<>(airportDto, HttpStatus.CREATED);
    }
}
