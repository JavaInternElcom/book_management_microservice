package com.elcom.library.validation;

import com.elcom.library.model.Author;
import com.elcom.library.model.Category;
import com.elcom.utils.StringUtil;

import javax.xml.bind.ValidationException;

public class CategoryValidation extends AbstractValidation{

    public String validateUpsert(Category category, String method) throws ValidationException {
        if(method.equalsIgnoreCase("UPDATE")){
            if(StringUtil.isNullOrEmpty(category.getUuid())){
                getMessageDescCollection().add("Uuid là bắt buộc");
            }
        }

        if(StringUtil.isNullOrEmpty(category.getCode())){
            getMessageDescCollection().add("Mã là bắt buộc");
        }
        if(StringUtil.isNullOrEmpty(category.getName())){
            getMessageDescCollection().add("Tên là bắt buộc");
        }
        return !isValid() ? this.buildValidationMessage() : null;
    }
}
