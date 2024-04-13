package com.example.api.services;

import com.example.api.dto.AirportDto;
import com.example.api.mappers.AirportMapper;
import com.example.api.models.Airport;
import com.example.api.repositories.AirportRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AirportService {
    private AirportRepository airportRepository;

    public AirportDto createAirport(AirportDto airportDto) {
        Airport airport = AirportMapper.mapToAirport(airportDto);
        airport = airportRepository.save(airport);
        return AirportMapper.mapToAirportDto(airport);
    }
}
