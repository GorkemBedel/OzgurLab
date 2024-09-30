package com.ozgur.Hospital.System.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AdminDto(
    String name,
    String surname,
    String password,
    String username
) {
}
