package com.ozgur.Hospital.System.repository;

import com.ozgur.Hospital.System.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByPatientUsername(String username);
}
