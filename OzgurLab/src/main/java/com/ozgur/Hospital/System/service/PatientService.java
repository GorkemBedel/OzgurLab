package com.ozgur.Hospital.System.service;

import com.ozgur.Hospital.System.dto.PatientDto;
import com.ozgur.Hospital.System.exception.AdminNotFoundException;
import com.ozgur.Hospital.System.exception.MissingParameterException;
import com.ozgur.Hospital.System.exception.UsernameNotUniqueException;
import com.ozgur.Hospital.System.model.Patient;
import com.ozgur.Hospital.System.model.Report;
import com.ozgur.Hospital.System.model.Role;
import com.ozgur.Hospital.System.repository.PatientRepository;
import com.ozgur.Hospital.System.repository.ReportRepository;
import com.ozgur.Hospital.System.rules.CreatePatientValidator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final ReportRepository reportRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CreatePatientValidator validator;


    public PatientService(PatientRepository patientRepository,
                          ReportRepository reportRepository,
                          BCryptPasswordEncoder bCryptPasswordEncoder,
                          CreatePatientValidator validator) {
        this.patientRepository = patientRepository;
        this.reportRepository = reportRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.validator = validator;
    }

    public Patient createPatient(PatientDto patientDto) {

        validator.validateCreatePatientDto(patientDto);
        validator.validateCreatePatientUsernameUnique(patientRepository, patientDto);

            Patient newPatient = Patient.builder()
                    .TC(patientDto.TC())
                    .name(patientDto.name())
                    .username(patientDto.username())
                    .password(bCryptPasswordEncoder.encode(patientDto.password()))
                    .role(Role.ROLE_USER)
                    .authorities(new HashSet<>(List.of(Role.ROLE_USER)))
                    .build();

            return patientRepository.save(newPatient);
        }

    public Optional<Patient> getByPatientUsername(String username) {
        return patientRepository.findByusername(username);
    }
    public Optional<Patient> getByPatientName(String name) {
        return patientRepository.findByName(name);
    }

    public List<Report> getReports() {
        String username = WhoAuthenticated.whoIsAuthenticated(); //authenticated patient.
        Patient authenticatedPatient = patientRepository.findByusername(username)
                .orElseThrow(() -> new AdminNotFoundException("You Are Not Authenticated"));

        return authenticatedPatient.getReports();



    }

    public List<Report> getReportsSortedByDate() {
        String username = WhoAuthenticated.whoIsAuthenticated(); //authenticated patient.
        Patient authenticatedPatient = patientRepository.findByusername(username)
                .orElseThrow(() -> new AdminNotFoundException("You Are Not Authenticated"));

        List<Report> reports = authenticatedPatient.getReports();
        reports.sort(Comparator.comparing(Report::getReportDate).reversed());
        return reports;
    }
}
