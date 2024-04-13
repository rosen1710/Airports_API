package com.example.api.mappers;

import com.example.api.dto.AirportDto;
import com.example.api.models.Airport;

public class AirportMapper {
    public static AirportDto mapToAirportDto(Airport airport) {
        return new AirportDto(
                airport.getId(),
                airport.getName(),
                CityMapper.mapToCityDto(airport.getCity()),
                airport.getIataCode(),
                airport.getIcaoCode(),
                airport.getLatitude(),
                airport.getLongitude()
        );
    }

    public static Airport mapToAirport(AirportDto airportDto) {
        return new Airport(
                airportDto.getId(),
                airportDto.getName(),
                CityMapper.mapToCity(airportDto.getCity()),
                airportDto.getIataCode(),
                airportDto.getIcaoCode(),
                airportDto.getLatitude(),
                airportDto.getLongitude()
        );
    }
}
