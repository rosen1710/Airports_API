package com.example.api.services;

import com.example.api.dto.AirportDto;
import com.example.api.dto.CityDto;
import com.example.api.dto.CountryDto;
import com.example.api.mappers.AirportMapper;
import com.example.api.models.Airport;
import com.example.api.models.AirportFilter;
import com.example.api.repositories.AirportRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AirportService {
    private AirportRepository airportRepository;

    public AirportDto createAirport(AirportDto airportDto) {
        Airport airport = AirportMapper.mapToAirport(airportDto);
        airport = airportRepository.save(airport);
        return AirportMapper.mapToAirportDto(airport);
    }

    public List<AirportDto> getAllAirports() {
        List<Object[]> airportObjects = airportRepository.getAllAirports();
        return airportObjects.stream().map(airportObject -> new AirportDto(
                (Long) airportObject[0],
                (String) airportObject[1],
                new CityDto(
                        (Long) airportObject[2],
                        (String) airportObject[3],
                        new CountryDto(
                                (String) airportObject[4],
                                (String) airportObject[5],
                                (String) airportObject[6]
                        ),
                        (String) airportObject[7]
                ),
                (String) airportObject[8],
                (String) airportObject[9],
                (Double) airportObject[10],
                (Double) airportObject[11]
        )).collect(Collectors.toList());
    }

    public List<AirportDto> getFilteredRootAirport(AirportFilter airportFilter) {
        List<Object[]> airportObjects = airportRepository.getFilteredRootAirport(airportFilter.countryIso2Codes(), airportFilter.cityIds(),
                airportFilter.airportIcaoCodes(), airportFilter.airportNames());
        return airportObjects.stream().map(airportObject -> new AirportDto(
                (Long) airportObject[0],
                (String) airportObject[1],
                new CityDto(
                        (Long) airportObject[2],
                        (String) airportObject[3],
                        new CountryDto(
                                (String) airportObject[4],
                                (String) airportObject[5],
                                (String) airportObject[6]
                        ),
                        (String) airportObject[7]
                ),
                (String) airportObject[8],
                (String) airportObject[9],
                (Double) airportObject[10],
                (Double) airportObject[11]
        )).collect(Collectors.toList());
    }
}
