package com.example.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CityWithAirportsDto {
    private Long id;
    private String name;
    private String timezone;
    private Set<AirportEndDto> airports;
}
