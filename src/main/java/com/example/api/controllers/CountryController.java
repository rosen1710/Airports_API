package com.example.api.controllers;

import com.example.api.dto.CountryDto;
import com.example.api.services.CountryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("api/v0/countries")
public class CountryController {
    private CountryService countryService;

    @PostMapping
    public ResponseEntity<CountryDto> createCountry(@RequestBody CountryDto countryDto) {
        countryDto = countryService.createCountry(countryDto);
        return new ResponseEntity<>(countryDto, HttpStatus.CREATED);
    }
}
