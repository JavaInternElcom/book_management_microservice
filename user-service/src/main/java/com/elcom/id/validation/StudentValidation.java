package com.elcom.id.validation;

import com.elcom.id.model.Student;
import com.elcom.id.model.dto.StudentDTO;
import com.elcom.utils.StringUtil;

import javax.xml.bind.ValidationException;

public class StudentValidation extends AbstractValidation {

    public String validateUpsert(Student student, String method) throws ValidationException {
        if(method.equalsIgnoreCase("UPDATE")){
            if(StringUtil.isNullOrEmpty(student.getUuid())){
                getMessageDescCollection().add("Uuid là bắt buộc");
            }
        }

        if(StringUtil.isNullOrEmpty(student.getLastName())){
            getMessageDescCollection().add("Họ là bắt buộc");
        }
        if(StringUtil.isNullOrEmpty(student.getFirstName())){
            getMessageDescCollection().add("Tên là bắt buộc");
        }
        if(StringUtil.isNullOrEmpty(student.getPhoneNumber())){
            getMessageDescCollection().add("Số điện thoại là bắt buộc");
        }
        if(StringUtil.isNullOrEmpty(student.getEmail())){
            getMessageDescCollection().add("Email là bắt buộc");
        }
        if(StringUtil.isNullOrEmpty(student.getAddress())){
            getMessageDescCollection().add("Địa chỉ là bắt buộc");
        }
        if(student.getGender() == null){
            getMessageDescCollection().add("Giới tính là bắt buộc");
        }

        if(!StringUtil.isNullOrEmpty(student.getPhoneNumber())
                && !StringUtil.checkMobilePhoneNumberNew(student.getPhoneNumber())){
            getMessageDescCollection().add("Số điện thoại không hợp lệ");
        }
        if(!StringUtil.isNullOrEmpty(student.getEmail())
                && !StringUtil.validateEmail(student.getEmail())){
            getMessageDescCollection().add("Email không hợp lệ");
        }
        return !isValid() ? this.buildValidationMessage() : null;
    }
}
