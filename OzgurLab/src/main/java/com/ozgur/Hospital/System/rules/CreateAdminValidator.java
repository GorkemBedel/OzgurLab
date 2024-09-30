package com.ozgur.Hospital.System.rules;

import com.ozgur.Hospital.System.dto.AdminDto;
import com.ozgur.Hospital.System.exception.MissingParameterException;
import com.ozgur.Hospital.System.exception.UsernameNotUniqueException;
import com.ozgur.Hospital.System.repository.AdminRepository;
import org.springframework.stereotype.Component;

@Component
public class CreateAdminValidator {

    public void validateCreateAdminAdminDto(AdminDto adminDto) {
        if (adminDto.name() == null) {
            throw new MissingParameterException("You must provide your name.");
        }
        if (adminDto.surname() == null) {
            throw new MissingParameterException("You must provide your surname.");
        }
        if (adminDto.username() == null) {
            throw new MissingParameterException("You must provide your username.");
        }
        if (adminDto.password() == null) {
            throw new MissingParameterException("You must provide your password.");
        }
    }

    public void validateCreateAdminUsernameUnique(AdminRepository adminRepository, String username) {
        if(adminRepository.existsByUsername(username)) {
            throw new UsernameNotUniqueException("That username already exists");
        }
    }


}
