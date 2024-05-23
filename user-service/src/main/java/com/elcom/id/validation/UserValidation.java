package com.elcom.id.validation;

import com.elcom.utils.StringUtil;

import javax.xml.bind.ValidationException;

public class UserValidation extends AbstractValidation{

    public String validateLogin(String username, String password) throws ValidationException {
        if(StringUtil.isNullOrEmpty(username)){
            getMessageDescCollection().add("Tên đăng nhập là bắt buộc");
        }
        if(StringUtil.isNullOrEmpty(password)){
            getMessageDescCollection().add("Mật khẩu là bắt buộc");
        }
        return !isValid() ? this.buildValidationMessage() : null;
    }
}
