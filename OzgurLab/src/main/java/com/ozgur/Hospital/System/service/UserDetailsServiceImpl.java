package com.ozgur.Hospital.System.service;

import com.ozgur.Hospital.System.model.Admin;
import com.ozgur.Hospital.System.model.Laborant;
import com.ozgur.Hospital.System.model.Patient;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AdminService adminService;
    private final LaborantService laborantService;
    private final PatientService patientService;

    public UserDetailsServiceImpl(AdminService adminService, LaborantService laborantService, PatientService patientService) {
        this.adminService = adminService;
        this.laborantService = laborantService;
        this.patientService = patientService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Laborant> laborant = laborantService.getByLaborantUsername(username);
        Optional<Admin> admin = adminService.getByAdminName(username);
        Optional<Patient> patient = patientService.getByPatientUsername(username);

        if(laborant.isPresent()){
            return laborant.get();
        }else if(admin.isPresent()){
            return admin.get();
        }else if(patient.isPresent()){
            return patient.get();
        }
        else{
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}
