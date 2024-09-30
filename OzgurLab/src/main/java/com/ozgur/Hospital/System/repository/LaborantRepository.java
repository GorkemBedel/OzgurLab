package com.ozgur.Hospital.System.repository;

import com.ozgur.Hospital.System.model.Admin;
import com.ozgur.Hospital.System.model.Laborant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LaborantRepository extends JpaRepository<Laborant, Long> {
    Optional<Laborant> findByusername (String username);
    Boolean existsByUsername(String username);
}
