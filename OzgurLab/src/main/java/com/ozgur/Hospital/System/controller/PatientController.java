package com.ozgur.Hospital.System.controller;

import com.ozgur.Hospital.System.dto.PatientDto;
import com.ozgur.Hospital.System.model.Patient;
import com.ozgur.Hospital.System.model.Report;
import com.ozgur.Hospital.System.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/patient")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }
    @PostMapping("/createPatient")
    public ResponseEntity<Patient> createPatient(@RequestBody PatientDto patientDto){
        return ResponseEntity.ok(patientService.createPatient(patientDto));
    }

    @GetMapping("/getReports")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Report>> getReports(){
        return ResponseEntity.ok(patientService.getReports());
    }

    @GetMapping("/getReportsSortedByDate")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Report>> getReportsSortedByDate(){
        return ResponseEntity.ok(patientService.getReportsSortedByDate());
    }





}