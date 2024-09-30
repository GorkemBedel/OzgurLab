package com.ozgur.Hospital.System.service;

import com.ozgur.Hospital.System.dto.AdminDto;
import com.ozgur.Hospital.System.dto.CreateLaborantRequest;
import com.ozgur.Hospital.System.dto.ReportDto;
import com.ozgur.Hospital.System.exception.AdminNotFoundException;
import com.ozgur.Hospital.System.exception.MissingParameterException;
import com.ozgur.Hospital.System.exception.UsernameNotUniqueException;
import com.ozgur.Hospital.System.model.*;
import com.ozgur.Hospital.System.repository.LaborantRepository;
import com.ozgur.Hospital.System.repository.LaborantRequestRepository;
import com.ozgur.Hospital.System.repository.ReportRepository;
import com.ozgur.Hospital.System.rules.CreateLaborantRequestValidator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class LaborantService {

    private final LaborantRepository laborantRepository;
    private final LaborantRequestRepository laborantRequestRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CreateLaborantRequestValidator validator;
    private final ReportRepository reportRepository;


    public LaborantService(LaborantRepository laborantRepository,
                           LaborantRequestRepository laborantRequestRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder,
                           CreateLaborantRequestValidator validator,
                           ReportRepository reportRepository) {
        this.laborantRepository = laborantRepository;
        this.laborantRequestRepository = laborantRequestRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.validator = validator;
        this.reportRepository = reportRepository;
    }

    public LaborantRequest createNewLaborantRequest(CreateLaborantRequest createLaborantRequest) {

        validator.validateCreateLaborantRequest(createLaborantRequest);

        if(laborantRepository.existsByUsername(createLaborantRequest.username())){
            throw new UsernameNotUniqueException("That username already exists");
        }

        LaborantRequest laborantRequest = LaborantRequest.builder()
                .name(createLaborantRequest.name())
                .surname(createLaborantRequest.surname())
                .department(createLaborantRequest.department())
                .username(createLaborantRequest.username())
                .password(bCryptPasswordEncoder.encode(createLaborantRequest.password()))
                .build();

        return laborantRequestRepository.save(laborantRequest);
    }

    public Laborant createNewLaborantByLaborantRequest(LaborantRequest laborantRequest) {

        if(laborantRepository.existsByUsername(laborantRequest.getUsername())){
            throw new UsernameNotUniqueException("That username already exists");
        }

        Long randomId = RandomHospitalIdGenerator.generateUniqueSevenDigitId();
        return Laborant.builder()
                .laborant_id(randomId)
                .name(laborantRequest.getName())
                .surname(laborantRequest.getSurname())
                .department(laborantRequest.getDepartment())
                .username(laborantRequest.getUsername())
                .password(laborantRequest.getPassword()) //Laborant Request's password has already been encoded, so I wont encode it again
                .role(Role.ROLE_LABORANT)
                .authorities(new HashSet<>(List.of(Role.ROLE_LABORANT)))
                .build();
    }

    public Optional<Laborant> getByLaborantUsername(String username){
        return laborantRepository.findByusername(username);
    }

    public List<Laborant> getAllLaborants(){
        return laborantRepository.findAll();
    }





}
