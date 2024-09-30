package com.ozgur.Hospital.System.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ReportDto(
    String sickness,
    String sicknessDetails,
    Long laborantId,
    Long patientTC,
    Long Id,
    LocalDate reportDate
    ) {
}
