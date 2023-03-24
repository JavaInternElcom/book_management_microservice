package com.elcom.library.validation;

import com.elcom.library.model.BookDTO;
import com.elcom.library.model.Category;
import com.elcom.utils.StringUtil;

import javax.xml.bind.ValidationException;

public class BookValidation extends AbstractValidation{

    public String validateUpsert(BookDTO bookDTO, String method) throws ValidationException {
        if(method.equalsIgnoreCase("UPDATE")){
            if(StringUtil.isNullOrEmpty(bookDTO.getUuid())){
                getMessageDescCollection().add("Uuid là bắt buộc");
            }
        }

        if(StringUtil.isNullOrEmpty(bookDTO.getName())){
            getMessageDescCollection().add("Tên là bắt buộc");
        }
        if(StringUtil.isNullOrEmpty(bookDTO.getAuthorId())){
            getMessageDescCollection().add("Tác giả là bắt buộc");
        }
        if(StringUtil.isNullOrEmpty(bookDTO.getCategoryId())){
            getMessageDescCollection().add("Thể loại là bắt buộc");
        }
        return !isValid() ? this.buildValidationMessage() : null;
    }
}
