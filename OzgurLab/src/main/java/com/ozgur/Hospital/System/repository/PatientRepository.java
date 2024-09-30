package com.ozgur.Hospital.System.repository;

import com.ozgur.Hospital.System.model.Admin;
import com.ozgur.Hospital.System.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByusername (String username);
    Optional<Patient> findByName (String name);

    Boolean existsByUsername(String username);
    Boolean existsByName(String name);
    Boolean existsByTC(Long tc);
}
