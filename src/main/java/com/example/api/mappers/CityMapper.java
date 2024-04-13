package com.example.api.mappers;

import com.example.api.dto.CityDto;
import com.example.api.models.City;

public class CityMapper {
    public static CityDto mapToCityDto(City city) {
        return new CityDto(
                city.getId(),
                city.getName(),
                CountryMapper.mapToCountryDto(city.getCountry()),
                city.getTimezone()
        );
    }

    public static City mapToCity(CityDto cityDto) {
        return new City(
                cityDto.getId(),
                cityDto.getName(),
                CountryMapper.mapToCountry(cityDto.getCountry()),
                cityDto.getTimezone()
        );
    }
}
