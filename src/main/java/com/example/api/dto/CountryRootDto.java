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
public class CountryRootDto {
    private String name;
    private String iso2CountryCode;
    private String iso3CountryCode;
    private Set<CityWithAirportsDto> cities;
}
