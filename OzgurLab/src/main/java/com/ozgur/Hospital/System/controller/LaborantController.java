package com.ozgur.Hospital.System.controller;

import com.ozgur.Hospital.System.dto.CreateLaborantRequest;
import com.ozgur.Hospital.System.dto.ReportDto;
import com.ozgur.Hospital.System.model.Laborant;
import com.ozgur.Hospital.System.model.LaborantRequest;
import com.ozgur.Hospital.System.model.Report;
import com.ozgur.Hospital.System.service.LaborantService;
import com.ozgur.Hospital.System.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/v1/laborant")
public class LaborantController {

    private final LaborantService laborantService;
    private final ReportService reportService;

    public LaborantController(LaborantService laborantService, ReportService reportService) {
        this.laborantService = laborantService;
        this.reportService = reportService;
    }

    @PostMapping("/createLaborantRequest")
    public ResponseEntity<LaborantRequest> createLaborantRequest(@RequestBody CreateLaborantRequest laborantRequest) {
        return ResponseEntity.ok(laborantService.createNewLaborantRequest(laborantRequest));
    }

    @GetMapping("/allLaborants")
    public ResponseEntity<List<Laborant>> getAllLaborants(){
        return ResponseEntity.ok(laborantService.getAllLaborants());
    }


    @PutMapping("/updateReport")
    public ResponseEntity<Report> updateReport(@RequestParam("file") MultipartFile file,
                                              @ModelAttribute ReportDto reportDto) {
        return ResponseEntity.ok(reportService.updateReport(file, reportDto));
    }
}
