package com.ozgur.Hospital.System.repository;

import com.ozgur.Hospital.System.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByusername (String username);
    Boolean existsByUsername(String username);
    void deleteByUsername(String username);

}
