package com.ozgur.Hospital.System.rules;

import com.ozgur.Hospital.System.dto.PatientDto;
import com.ozgur.Hospital.System.exception.MissingParameterException;
import com.ozgur.Hospital.System.exception.UsernameNotUniqueException;
import com.ozgur.Hospital.System.repository.AdminRepository;
import com.ozgur.Hospital.System.repository.PatientRepository;
import org.springframework.stereotype.Component;

@Component
public class CreatePatientValidator {

    public void validateCreatePatientDto(PatientDto patientDto){
        if (patientDto.name() == null) {
            throw new MissingParameterException("You must provide your name.");
        }
        if (patientDto.username() == null) {
            throw new MissingParameterException("You must provide your username.");
        }
        if (patientDto.password() == null) {
            throw new MissingParameterException("You must provide your password.");
        }
        if (patientDto.TC() == null) {
            throw new MissingParameterException("You must provide your TC.");
        }
    }


    public void validateCreatePatientUsernameUnique(PatientRepository patientRepository, PatientDto patientDto ) {
        if (patientRepository.existsByTC(patientDto.TC())) {
            throw new UsernameNotUniqueException("That TC already exists");
        }

        if (patientRepository.existsByUsername(patientDto.username())) {
            throw new UsernameNotUniqueException("That username already exists");
        }
        if (patientRepository.existsByName(patientDto.name())) {
            throw new UsernameNotUniqueException("That name already exists");
        }
    }
}
