package com.ozgur.Hospital.System.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PatientDto(
    String name,
    String username,
    String password,
    Long TC
) {

}
