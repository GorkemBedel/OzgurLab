package com.ozgur.Hospital.System.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CreateLaborantRequest(
    String name,
    String surname,
    String department,
    String username,
    String password
) {
}
