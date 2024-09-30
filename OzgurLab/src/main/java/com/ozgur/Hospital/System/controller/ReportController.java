package com.ozgur.Hospital.System.controller;

import com.ozgur.Hospital.System.dto.ReportDto;
import com.ozgur.Hospital.System.model.Report;
import com.ozgur.Hospital.System.model.ReportPhotoDto;
import com.ozgur.Hospital.System.repository.ReportRepository;
import com.ozgur.Hospital.System.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/v1/report")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/createReport")
    public ResponseEntity<Report> uploadPhoto(@RequestParam("file") MultipartFile file,
                                              @ModelAttribute ReportDto reportDto) {
        return ResponseEntity.ok(reportService.addReport(file, reportDto));
    }

    @GetMapping("/getAllReports")
    public ResponseEntity<List<Report>> getAllReports() {
        return ResponseEntity.ok(reportService.getAllReports());
    }

    @GetMapping("/getAllReportsSortedByDate")
    public ResponseEntity<List<Report>> getAllReportsSortedByDate() {
        return ResponseEntity.ok(reportService.getAllReportsSortedByDate());
    }

    @GetMapping("/getReportPhoto/{reportId}")
    public ResponseEntity<ReportPhotoDto> getReportPhoto(@PathVariable Long reportId) {
        return ResponseEntity.ok(reportService.getReportPhoto(reportId));
    }

}
