package com.example.api.services;

import com.example.api.dto.CityDto;
import com.example.api.mappers.CityMapper;
import com.example.api.models.City;
import com.example.api.repositories.CityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CityService {
    private CityRepository cityRepository;

    public CityDto createCity(CityDto cityDto) {
        City city = CityMapper.mapToCity(cityDto);
        city = cityRepository.save(city);
        return CityMapper.mapToCityDto(city);
    }
}
