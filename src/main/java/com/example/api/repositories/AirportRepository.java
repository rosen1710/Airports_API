package com.example.api.repositories;

import com.example.api.models.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface AirportRepository extends JpaRepository<Airport, Long> {
    @Query(value = """
            SELECT a.id, a.name, c.id, c.name, co.name, co.iso2country_code, co.iso3country_code, c.timezone, a.iata_code, a.icao_code, a.latitude, a.longitude
            FROM airport a
            JOIN city c ON a.city_id = c.id
            JOIN country co ON c.country_iso2country_code = co.iso2country_code;
            """,
    nativeQuery = true)
    List<Object[]> getAllAirports();

    @Query(value = """
            SELECT a.id, a.name, c.id, c.name, co.name, co.iso2country_code, co.iso3country_code, c.timezone, a.iata_code, a.icao_code, a.latitude, a.longitude
            FROM airport a
            JOIN city c ON a.city_id = c.id
            JOIN country co ON c.country_iso2country_code = co.iso2country_code
            WHERE (:countryIso2Codes IS NULL OR co.iso2country_code IN :countryIso2Codes)
            OR (:cityIds IS NULL OR c.id IN :cityIds)
            OR (:airportIcaoCodes IS NULL OR a.icao_code IN :airportIcaoCodes)
            OR (:airportNames IS NULL OR a.name IN :airportNames);
            """,
    nativeQuery = true)
    List<Object[]> getFilteredRootAirport(Set<String> countryIso2Codes, Set<Long> cityIds, Set<String> airportIcaoCodes, Set<String> airportNames);
}
