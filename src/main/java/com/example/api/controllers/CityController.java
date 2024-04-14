package com.example.api.controllers;

import com.example.api.dto.CityDto;
import com.example.api.services.CityService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v0/cities")
public class CityController {
    private CityService cityService;

    @PostMapping
    public ResponseEntity<CityDto> createCity(@RequestBody CityDto cityDto) {
        cityDto = cityService.createCity(cityDto);
        return new ResponseEntity<>(cityDto, HttpStatus.CREATED);
    }
}
