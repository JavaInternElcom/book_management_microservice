package com.elcom.id.validation;

import com.elcom.utils.StringUtil;

import javax.xml.bind.ValidationException;

public class UserValidation extends AbstractValidation{

    public String validateLogin(String username, String password) throws ValidationException {
        if(StringUtil.isNullOrEmpty(username)){
            getMessageDescCollection().add("Username is required!");
        }
        if(StringUtil.isNullOrEmpty(password)){
            getMessageDescCollection().add("Password is required!");
        }
        return !isValid() ? this.buildValidationMessage() : null;
    }
}
