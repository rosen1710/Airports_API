package com.example.api.controllers;

import com.example.api.dto.AirportDto;
import com.example.api.dto.CountryRootDto;
import com.example.api.models.AirportFilter;
import com.example.api.services.AirportService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
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
    public ResponseEntity<List<?>> getFilteredRootAirport(@RequestParam("countryAsRoot") boolean countryAsRoot, @RequestParam("page") int page, @RequestParam("size") int size, @RequestBody AirportFilter airportFilter) {
        if (countryAsRoot) {
            List<CountryRootDto> countryObjects = airportService.getFilteredRootCountry(airportFilter, PageRequest.of(page, size));
            return ResponseEntity.ok(countryObjects);
        }
        else {
            List<AirportDto> airportObjects = airportService.getFilteredRootAirport(airportFilter, PageRequest.of(page, size));
            return ResponseEntity.ok(airportObjects);
        }
    }
}
