package com.example.api.services;

import com.example.api.dto.*;
import com.example.api.mappers.AirportMapper;
import com.example.api.models.Airport;
import com.example.api.models.AirportFilter;
import com.example.api.models.Country;
import com.example.api.repositories.AirportRepository;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AirportService {
    private AirportRepository airportRepository;
    private EntityManager entityManager;

    public AirportDto createAirport(AirportDto airportDto) {
        Airport airport = AirportMapper.mapToAirport(airportDto);
        airport = airportRepository.save(airport);
        return AirportMapper.mapToAirportDto(airport);
    }

    public List<AirportDto> getAllAirports() {
        return entityManager.createQuery("SELECT a FROM Airport a JOIN FETCH a.city c JOIN FETCH c.country co", Airport.class)
                .getResultList().stream().map(airport -> new AirportDto(
                        airport.getId(),
                        airport.getName(),
                        new CityDto(
                                airport.getCity().getId(),
                                airport.getCity().getName(),
                                new CountryDto(
                                        airport.getCity().getCountry().getName(),
                                        airport.getCity().getCountry().getIso2CountryCode(),
                                        airport.getCity().getCountry().getIso3CountryCode()
                                ),
                                airport.getCity().getTimezone()
                        ),
                        airport.getIataCode(),
                        airport.getIcaoCode(),
                        airport.getLatitude(),
                        airport.getLongitude()
                )).toList();
    }

    public List<AirportDto> getFilteredRootAirport(AirportFilter airportFilter, Pageable pageable) {
        return entityManager.createQuery("SELECT a FROM Airport a JOIN FETCH a.city c JOIN FETCH c.country co WHERE co.iso2CountryCode IN ?1 OR c.id IN ?2 OR a.icaoCode IN ?3 OR a.name IN ?4", Airport.class)
                .setParameter(1, airportFilter.countryIso2Codes())
                .setParameter(2, airportFilter.cityIds())
                .setParameter(3, airportFilter.airportIcaoCodes())
                .setParameter(4, airportFilter.airportNames())
                .setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
                .setMaxResults(pageable.getPageSize())
                .getResultList().stream().map(airport -> new AirportDto(
                        airport.getId(),
                        airport.getName(),
                        new CityDto(
                                airport.getCity().getId(),
                                airport.getCity().getName(),
                                new CountryDto(
                                        airport.getCity().getCountry().getName(),
                                        airport.getCity().getCountry().getIso2CountryCode(),
                                        airport.getCity().getCountry().getIso3CountryCode()
                                ),
                                airport.getCity().getTimezone()
                        ),
                        airport.getIataCode(),
                        airport.getIcaoCode(),
                        airport.getLatitude(),
                        airport.getLongitude()
                )).toList();
    }

    public List<CountryRootDto> getFilteredRootCountry(AirportFilter airportFilter, Pageable pageable) {
        return entityManager.createQuery("SELECT co FROM Country co JOIN FETCH co.cities c JOIN FETCH c.airports a WHERE co.iso2CountryCode IN ?1 OR c.id IN ?2 OR a.icaoCode IN ?3 OR a.name IN ?4", Country.class)
                .setParameter(1, airportFilter.countryIso2Codes())
                .setParameter(2, airportFilter.cityIds())
                .setParameter(3, airportFilter.airportIcaoCodes())
                .setParameter(4, airportFilter.airportNames())
                .setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
                .setMaxResults(pageable.getPageSize())
                .getResultList().stream().map(country -> new CountryRootDto(
                        country.getName(),
                        country.getIso2CountryCode(),
                        country.getIso3CountryCode(),
                        country.getCities().stream().map(city -> new CityWithAirportsDto(
                                city.getId(),
                                city.getName(),
                                city.getTimezone(),
                                city.getAirports().stream().map(airport -> new AirportEndDto(
                                        airport.getId(),
                                        airport.getName(),
                                        airport.getIataCode(),
                                        airport.getIcaoCode(),
                                        airport.getLatitude(),
                                        airport.getLongitude()
                                )).collect(Collectors.toSet())
                        )).collect(Collectors.toSet())
                )).toList();
    }
}
