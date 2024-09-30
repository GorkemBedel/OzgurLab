package com.ozgur.Hospital.System.service;

import com.ozgur.Hospital.System.dto.ReportDto;
import com.ozgur.Hospital.System.exception.AdminNotFoundException;
import com.ozgur.Hospital.System.exception.MissingParameterException;
import com.ozgur.Hospital.System.exception.UnauthorizedException;
import com.ozgur.Hospital.System.model.Laborant;
import com.ozgur.Hospital.System.model.Patient;
import com.ozgur.Hospital.System.model.Report;
import com.ozgur.Hospital.System.model.ReportPhotoDto;
import com.ozgur.Hospital.System.repository.LaborantRepository;
import com.ozgur.Hospital.System.repository.PatientRepository;
import com.ozgur.Hospital.System.repository.ReportRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ReportService {

    Logger logger = LoggerFactory.getLogger(ReportService.class);
    private final ReportRepository reportRepository;
    private final LaborantRepository laborantRepository;
    private final PatientRepository patientRepository;

    public ReportService(ReportRepository reportRepository,
                         LaborantRepository laborantRepository,
                         PatientRepository patientRepository) {
        this.reportRepository = reportRepository;
        this.laborantRepository = laborantRepository;
        this.patientRepository = patientRepository;
    }

    public Report addReport(MultipartFile file, ReportDto reportDto) {

        Laborant laborant =  laborantRepository.findById(reportDto.laborantId())
                .orElseThrow(() -> new AdminNotFoundException("There is no laborant with id : " + reportDto.laborantId()));

        Patient patient =  patientRepository.findById(reportDto.patientTC())
                .orElseThrow(() -> new AdminNotFoundException("There is no patient with TC : " + reportDto.patientTC()));


        if (file.isEmpty()) {
            throw new MissingParameterException("No file uploaded.");
        }
        try {
            Report report = Report.builder()
                    .report_id(RandomHospitalIdGenerator.generateUniqueSevenDigitId())
                    .patientName(patient.getName())
                    .sickness(reportDto.sickness())
                    .sicknessDetails(reportDto.sicknessDetails())
                    .reportDate(LocalDate.now())
                    .reportPhoto(file.getBytes())
                    .patient(patient)
                    .laborant(laborant)
                    .patientTC(patient.getTC())
                    .laborantId(laborant.getLaborant_id())
                    .build();
            return reportRepository.save(report);
        } catch (IOException e) {
            throw new MissingParameterException("Error uploading photo.");
        }
    }


    public List<Report> getAllReports() {

        return reportRepository.findAll();
    }

    public List<Report> getAllReportsSortedByDate() {

        List<Report> reports = reportRepository.findAll();
        reports.sort(Comparator.comparing(Report::getReportDate).reversed());
        return reports;
    }

    public Report updateReport(MultipartFile file, ReportDto reportDto) {

        //Checking if the laborant is trying to update his/her OWN report
        String loggedInUsername = WhoAuthenticated.whoIsAuthenticated();

        Report toBeUpdatedReport = reportRepository.findById(reportDto.Id())
                .orElseThrow(() -> new UsernameNotFoundException("There is no report with id : " +  reportDto.Id()));

        Optional<Laborant> laborant = laborantRepository.findByusername(loggedInUsername);
        List<Report> reports = laborant.get().getReports();

        if(!reports.contains(toBeUpdatedReport)){ // If the report does not belong to the authenticated laborant
            throw new MissingParameterException("You don't have permission to update this report");
        }else {
            if(reportDto.sickness() != null){
                toBeUpdatedReport.setSickness(reportDto.sickness());
            }
            if(reportDto.sicknessDetails()!= null){
                toBeUpdatedReport.setSicknessDetails(reportDto.sicknessDetails());
            }
            if(reportDto.laborantId()!= null){
                //first remove that report from old laborant
                List<Report>  reportsOfOldLaborant = laborant.get().getReports();
                reportsOfOldLaborant.remove(toBeUpdatedReport);
                laborant.get().setReports(reportsOfOldLaborant);
                laborantRepository.save(laborant.get());

                //then add that report to the new laborant
                Laborant toBeUpdatedLaborant = laborantRepository.findById(reportDto.laborantId())
                        .orElseThrow(() -> new UsernameNotFoundException("There is no laborant with id : " + reportDto.laborantId()));
                toBeUpdatedReport.setLaborant(toBeUpdatedLaborant);
                toBeUpdatedLaborant.getReports().add(toBeUpdatedReport);
                laborantRepository.save(toBeUpdatedLaborant);
            }
            if(reportDto.reportDate()!= null){
                toBeUpdatedReport.setReportDate(reportDto.reportDate());
            }
        }
        return reportRepository.save(toBeUpdatedReport);
    }

    public ReportPhotoDto getReportPhoto(Long reportId) {

        Report reportToGetPhoto = reportRepository.findById(reportId)
                .orElseThrow(() -> new AdminNotFoundException("There is no report with id : " + reportId));

        String authenticatedUsername = WhoAuthenticated.whoIsAuthenticated();

        Optional<Patient> authenticatedPatient = patientRepository.findByusername(authenticatedUsername);
        Optional<Laborant> authenticatedLaborant = laborantRepository.findByusername(authenticatedUsername);

        if(authenticatedLaborant.isPresent()){
            List<Report> reportsOfLaborant = authenticatedLaborant.get().getReports();
            if(reportsOfLaborant.contains(reportToGetPhoto)){ // If the report does not belong to the authenticated laborant
                return new ReportPhotoDto(reportToGetPhoto.getReportPhoto());
            }
        }else if(authenticatedPatient.isPresent()){
            List<Report> reportsOfPatient = authenticatedPatient.get().getReports();
            if(reportsOfPatient.contains(reportToGetPhoto)){ // If the report does not belong to the authenticated patient
                return new ReportPhotoDto(reportToGetPhoto.getReportPhoto());

            }
        }
        throw new UnauthorizedException("That report does not belong to you.");

    }
}
