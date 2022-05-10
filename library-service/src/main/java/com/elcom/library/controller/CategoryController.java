package com.elcom.library.controller;

import com.elcom.library.elasticsearch.service.CategoryEsService;
import com.elcom.library.model.Category;
import com.elcom.library.model.dto.AuthorizationResponseDTO;
import com.elcom.library.service.CategoryService;
import com.elcom.message.MessageContent;
import com.elcom.message.ResponseMessage;
import com.elcom.utils.StringUtil;
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

    @Autowired
    private CategoryEsService categoryEsService;

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

    // create new category
    public ResponseMessage createCategory(String requestPath, String method, Map<String, String> headerParam,
                                          Map<String, Object> bodyParam){
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            String name = (String) bodyParam.get("name");

            Category findCategoryByCode = categoryService.findByCode(name.toUpperCase());

            if(findCategoryByCode != null){
                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Category code existed",
                        new MessageContent(HttpStatus.BAD_REQUEST.value(), "Category code existed", null));
            } else {
                Category category = new Category();
                category.setUuid(StringUtil.randomUUID());
                category.setName(name);
                category.setCode(name.toUpperCase());

                // save data to database
                categoryService.save(category);
                // save data to elasticsearch
                categoryEsService.save(category);

                response = new ResponseMessage(HttpStatus.OK.value(), "Create new category successfully.",
                        new MessageContent(HttpStatus.OK.value(), "Create new category successfully.", category));
            }
        }
        return response;
    }

    // update category by uuid
    public ResponseMessage updateCategoryById(Map<String, Object> bodyParam, Map<String, String> headerParam,
                                              String pathParam, String method, String requestPath){
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            String uuid = pathParam;
            if(uuid == null){
                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Id is required in requestParam",
                        new MessageContent(HttpStatus.BAD_REQUEST.value(), "Id is required in requestParam", null));
            } else {
                String name = (String) bodyParam.get("name");

                Category category = categoryService.findByUuid(uuid);

                if(category == null){
                    response = new ResponseMessage(HttpStatus.NOT_FOUND.value(), "Not found category with uuid " + uuid,
                            new MessageContent(HttpStatus.NOT_FOUND.value(), "Not found category with uuid " + uuid, null));
                } else {
                    category.setUuid(uuid);
                    category.setName(name);
                    category.setCode(category.getCode());

                    categoryService.save(category);

                    response = new ResponseMessage(HttpStatus.OK.value(), "Update category successfully.",
                            new MessageContent(HttpStatus.OK.value(), "Update category successfully.", category));
                }
            }
        }
        return response;
    }

    // delete category by uuid
    public ResponseMessage deleteCategoryById(String requestPath, Map<String, String> headerParam, String pathParam, String requestMethod){
        ResponseMessage response = null;

        AuthorizationResponseDTO dto = authenToken(headerParam);
        if(dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "You are not logged in.",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "You are not logged in", null));
        } else {
            String uuid = pathParam;
            if (uuid == null) {
                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Id is required in requestParam",
                        new MessageContent(HttpStatus.BAD_REQUEST.value(), "Id is required in requestParam", null));
            } else {
                Category category = categoryService.findByUuid(uuid);
                if(category == null){
                    response = new ResponseMessage(HttpStatus.NOT_FOUND.value(), "Not found category with uuid " + uuid,
                            new MessageContent(HttpStatus.NOT_FOUND.value(), "Not found category with uuid " + uuid, null));
                } else {
                    categoryService.delete(uuid);
                    response = new ResponseMessage(HttpStatus.OK.value(), "Delete category successfully.",
                            new MessageContent(HttpStatus.OK.value(), "Delete category successfully.", null));
                }
            }
        }
        return response;
    }

}
