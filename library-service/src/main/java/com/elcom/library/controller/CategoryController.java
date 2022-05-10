package com.elcom.library.controller;

import com.elcom.library.model.dto.AuthorizationResponseDTO;
import com.elcom.library.service.CategoryService;
import com.elcom.message.MessageContent;
import com.elcom.message.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
public class CategoryController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

    // get all categories
    public ResponseMessage getAllCategories(Map<String, String> headerParam, String requestPath,
                                            String requestMethod, String urlParam){

        long start = System.currentTimeMillis();
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        long end = System.currentTimeMillis();
        LOGGER.info("getAllCategories >>> authenToken in {} ms", (end - start));
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            response = new ResponseMessage(HttpStatus.OK.value(), "Get all categories successfully.",
                    new MessageContent(HttpStatus.OK.value(), "Get all categories successfully.", categoryService.findAll()));
        }

        return response;
    }
}
