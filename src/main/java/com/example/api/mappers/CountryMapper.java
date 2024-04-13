package com.example.api.mappers;

import com.example.api.dto.CountryDto;
import com.example.api.models.Country;

public class CountryMapper {
    public static CountryDto mapToCountryDto(Country country) {
        return new CountryDto(
                country.getName(),
                country.getIso2CountryCode(),
                country.getIso3CountryCode()
        );
    }

    public static Country mapToCountry(CountryDto countryDto) {
        return new Country(
                countryDto.getName(),
                countryDto.getIso2CountryCode(),
                countryDto.getIso3CountryCode()
        );
    }
}
