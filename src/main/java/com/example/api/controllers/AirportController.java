package com.example.api.controllers;

import com.example.api.dto.AirportDto;
import com.example.api.models.AirportFilter;
import com.example.api.services.AirportService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v0/airports")
public class AirportController {
    private AirportService airportService;

    @PostMapping
    public ResponseEntity<AirportDto> createAirport(@RequestBody AirportDto airportDto) {
        airportDto = airportService.createAirport(airportDto);
        return new ResponseEntity<>(airportDto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AirportDto>> getAllAirports() {
        List<AirportDto> airportDtos = airportService.getAllAirports();
        return ResponseEntity.ok(airportDtos);
    }

    @PostMapping("/filter")
    public ResponseEntity<List<AirportDto>> getFilteredRootAirport(@RequestParam("countryAsRoot") boolean countryAsRoot, @RequestBody AirportFilter airportFilter) {
        System.out.println(countryAsRoot);
        List<AirportDto> airportObjects = airportService.getFilteredRootAirport(airportFilter);
        return ResponseEntity.ok(airportObjects);
    }
}
