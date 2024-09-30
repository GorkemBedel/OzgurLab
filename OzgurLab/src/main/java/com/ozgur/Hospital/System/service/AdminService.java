package com.ozgur.Hospital.System.service;

import com.ozgur.Hospital.System.dto.AdminDto;
import com.ozgur.Hospital.System.dto.PatientDto;
import com.ozgur.Hospital.System.dto.ReportDto;
import com.ozgur.Hospital.System.exception.AdminNotFoundException;
import com.ozgur.Hospital.System.exception.MissingParameterException;
import com.ozgur.Hospital.System.exception.UnauthorizedException;
import com.ozgur.Hospital.System.exception.UsernameNotUniqueException;
import com.ozgur.Hospital.System.model.*;
import com.ozgur.Hospital.System.repository.*;
import com.ozgur.Hospital.System.rules.CreateAdminValidator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final LaborantRepository laborantRepository;
    private final LaborantService laborantService;
    private final LaborantRequestRepository laborantRequestRepository;
    private final AdminRepository adminRepository;
    private final ReportRepository reportRepository;
    private final PatientRepository patientRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CreateAdminValidator validator;


    public AdminService(LaborantRepository laborantRepository,
                        LaborantRequestRepository laborantRequestRepository,
                        AdminRepository adminRepository,
                        ReportRepository reportRepository,
                        LaborantService laborantService,
                        BCryptPasswordEncoder bCryptPasswordEncoder,
                        CreateAdminValidator validator,
                        PatientRepository patientRepository) {
        this.laborantRepository = laborantRepository;
        this.laborantRequestRepository = laborantRequestRepository;
        this.adminRepository = adminRepository;
        this.reportRepository = reportRepository;
        this.laborantService = laborantService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.validator = validator;
        this.patientRepository = patientRepository;
    }

    public List<LaborantRequest> getAllLaborantRequests() {
        return laborantRequestRepository.findAll();
    }

    public List<Admin> getAllAdmins(){
        return adminRepository.findAll();
    }

    public Laborant approveLaborantRequest(Long id) {

        // get the laborant request by id of the LaborantRequest
        LaborantRequest laborantRequest = laborantRequestRepository.findById(id)
                .orElseThrow(() -> new AdminNotFoundException("There is no laborant request with id : " + id));

        Laborant newLaborant = laborantService.createNewLaborantByLaborantRequest(laborantRequest);

        // delete the laborant request from database after creating a new laborant
        laborantRequestRepository.deleteById(id);

        return laborantRepository.save(newLaborant);
    }

    public Optional<Admin> getByAdminName(String username) {
        return adminRepository.findByusername(username);
    }

    public Admin createAdmin(AdminDto adminDto) {

        validator.validateCreateAdminAdminDto(adminDto);
        validator.validateCreateAdminUsernameUnique(adminRepository, adminDto.username());

        Long randomId = RandomHospitalIdGenerator.generateUniqueSevenDigitId();

        Admin admin = Admin.builder()
                .admin_id(randomId)
                .name(adminDto.name())
                .surname(adminDto.surname())
                .username(adminDto.username())
                .password(bCryptPasswordEncoder.encode(adminDto.password()))
                .role(Role.ROLE_ADMIN)
                .authorities(new HashSet<>(List.of(Role.ROLE_ADMIN)))
                .build();
        return adminRepository.save(admin);
    }

    public Admin updateOwnAdminAccount(AdminDto adminDto) {

        //Checking if the user is trying to update his/her OWN review
        String loggedInUsername = WhoAuthenticated.whoIsAuthenticated();

        Admin toBeUpdatedAdmin = adminRepository.findByusername(loggedInUsername)
                .orElseThrow(() -> new UsernameNotFoundException("There is no admin with username : " +  loggedInUsername));

            if(adminDto.username() != null){
                validator.validateCreateAdminUsernameUnique(adminRepository,adminDto.username());
                toBeUpdatedAdmin.setUsername(adminDto.username());
            }

            if(adminDto.name() != null){
                toBeUpdatedAdmin.setName(adminDto.name());
            }
            if(adminDto.surname() != null){
                toBeUpdatedAdmin.setSurname(adminDto.surname());
            }
            if(adminDto.password() != null){
                toBeUpdatedAdmin.setPassword(bCryptPasswordEncoder.encode(adminDto.password()));
            }

        return adminRepository.save(toBeUpdatedAdmin);
    }

    @Transactional
    public void deleteAdminById(Long id) {
        Admin toBeDeletedAdmin = adminRepository.findById(id)
                .orElseThrow(() -> new AdminNotFoundException("There is no admin with id : " + id));

        //Checking if the user is trying to update his/her OWN review
        String loggedInUsername = WhoAuthenticated.whoIsAuthenticated();


        if(loggedInUsername.equals("Big Admin") && toBeDeletedAdmin.getUsername().equals("Big Admin")){
            throw new UnauthorizedException("Big Admin can not be deleted!!!!");
        }

        if(loggedInUsername.equals("Big Admin") || toBeDeletedAdmin.getUsername().equals(loggedInUsername)){ //Big Admin has permission for all operations || an admin trying to delete his/her own account
            adminRepository.deleteById(id);
        }else{ //Another admin is trying to delete an admin
            throw new UnauthorizedException("You are trying to update another user's informations.");
        }
    }

    @Transactional
    public void deleteAdminByUsername(String username) {
        Admin toBeDeletedAdmin = adminRepository.findByusername(username)
                .orElseThrow(() -> new AdminNotFoundException("There is no admin with username : " +  username));

        //Checking if the user is trying to update his/her OWN review
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = authentication.getName();

        if(loggedInUsername.equals("Big Admin")){
            throw new UnauthorizedException("Big Admin can not be deleted!!!!");
        }


        if(loggedInUsername.equals("Big Admin") || toBeDeletedAdmin.getUsername().equals(loggedInUsername)){ //Big Admin has permission for all operations || an admin trying to delete his/her own account
            adminRepository.deleteByUsername(username);
        }else{ //Another user
            throw new UnauthorizedException("You are trying to update another user's informations.");
        }


    }

    @Transactional
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient getByPatientName(String name) {
        return patientRepository.findByName(name)
                .orElseThrow(() -> new AdminNotFoundException("There is no patient with name : " + name));
    }


}
