package com.ozgur.Hospital.System.repository;

import com.ozgur.Hospital.System.model.LaborantRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LaborantRequestRepository extends JpaRepository<LaborantRequest, Long> {
}
