package com.example.api.services;

import com.example.api.dto.CountryDto;
import com.example.api.mappers.CountryMapper;
import com.example.api.models.Country;
import com.example.api.repositories.CountryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CountryService {
    private CountryRepository countryRepository;

    public CountryDto createCountry(CountryDto countryDto) {
        Country country = CountryMapper.mapToCountry(countryDto);
        country = countryRepository.save(country);
        return CountryMapper.mapToCountryDto(country);
    }
}
