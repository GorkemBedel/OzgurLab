package com.ozgur.Hospital.System.rules;

import com.ozgur.Hospital.System.dto.CreateLaborantRequest;
import com.ozgur.Hospital.System.exception.MissingParameterException;
import org.springframework.stereotype.Component;

@Component
public class CreateLaborantRequestValidator {

    public void validateCreateLaborantRequest(CreateLaborantRequest createLaborantRequest){
        if (createLaborantRequest.name() == null) {
            throw new MissingParameterException("You must provide your name.");
        }
        if (createLaborantRequest.surname() == null) {
            throw new MissingParameterException("You must provide your surname.");
        }
        if (createLaborantRequest.department() == null) {
            throw new MissingParameterException("You must provide your department.");
        }
        if (createLaborantRequest.username() == null) {
            throw new MissingParameterException("You must provide your username.");
        }
        if (createLaborantRequest.password() == null) {
            throw new MissingParameterException("You must provide your password.");
        }
    }
}
