package com.example.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AirportDto {
    private Long id;
    private String name;
    private CityDto city;
    private String iataCode;
    private String icaoCode;
    private Double latitude;
    private Double longitude;
}
