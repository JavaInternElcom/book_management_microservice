package com.elcom.bookloan.validation;

import com.elcom.utils.StringUtil;

import javax.xml.bind.ValidationException;

public class BookLoanValidation extends AbstractValidation{

    public String validateBorrowLog(String bookId, String studentId) throws ValidationException {
        if(StringUtil.isNullOrEmpty(bookId)){
            getMessageDescCollection().add("Sách là bắt buộc");
        }
        if(StringUtil.isNullOrEmpty(studentId)){
            getMessageDescCollection().add("Học sinh là bắt buộc");
        }
        return !isValid() ? this.buildValidationMessage() : null;
    }
}
