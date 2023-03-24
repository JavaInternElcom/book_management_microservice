package com.elcom.library.validation;

import com.elcom.library.model.Author;
import com.elcom.utils.StringUtil;

import javax.xml.bind.ValidationException;

public class AuthorValidation extends AbstractValidation{

    public String validateUpsert(Author author, String method) throws ValidationException {
        if(method.equalsIgnoreCase("UPDATE")){
            if(StringUtil.isNullOrEmpty(author.getUuid())){
                getMessageDescCollection().add("Uuid là bắt buộc");
            }
        }

        if(StringUtil.isNullOrEmpty(author.getLastName())){
            getMessageDescCollection().add("Họ là bắt buộc");
        }
        if(StringUtil.isNullOrEmpty(author.getFirstName())){
            getMessageDescCollection().add("Tên là bắt buộc");
        }
        if(StringUtil.isNullOrEmpty(author.getPhoneNumber())){
            getMessageDescCollection().add("Số điện thoại là bắt buộc");
        }
        if(StringUtil.isNullOrEmpty(author.getEmail())){
            getMessageDescCollection().add("Email là bắt buộc");
        }

        if(!StringUtil.isNullOrEmpty(author.getPhoneNumber())
                && !StringUtil.checkMobilePhoneNumberNew(author.getPhoneNumber())){
            getMessageDescCollection().add("Số điện thoại không hợp lệ");
        }
        if(!StringUtil.isNullOrEmpty(author.getEmail())
                && !StringUtil.validateEmail(author.getEmail())){
            getMessageDescCollection().add("Email không hợp lệ");
        }
        return !isValid() ? this.buildValidationMessage() : null;
    }
}
