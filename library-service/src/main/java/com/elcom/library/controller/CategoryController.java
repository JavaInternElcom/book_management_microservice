package com.elcom.library.controller;

import com.elcom.library.elasticsearch.service.CategoryEsService;
import com.elcom.library.model.Category;
import com.elcom.library.model.dto.AuthorizationResponseDTO;
import com.elcom.library.service.CategoryService;
import com.elcom.library.validation.CategoryValidation;
import com.elcom.message.MessageContent;
import com.elcom.message.ResponseMessage;
import com.elcom.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

import javax.xml.bind.ValidationException;
import java.util.Map;
import java.util.UUID;

@Controller
public class CategoryController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryEsService categoryEsService;

    // get all categories
    public ResponseMessage getAllCategories(Map<String, String> headerParam, String requestPath,
                                            String requestMethod, String urlParam) {

        long start = System.currentTimeMillis();
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        long end = System.currentTimeMillis();
        LOGGER.info("getAllCategories >>> authenToken in {} ms", (end - start));
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            response = new ResponseMessage(HttpStatus.OK.value(), "Lấy danh sách thể loại",
                    new MessageContent(HttpStatus.OK.value(), "Lấy danh sách thể loại", categoryService.findAll()));
        }

        return response;
    }

    // create new category
    public ResponseMessage createCategory(String requestPath, String method, Map<String, String> headerParam,
                                          Map<String, Object> bodyParam) throws ValidationException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            Category category = setCategoryFromBodyParam(bodyParam);
            String invalidData = new CategoryValidation().validateUpsert(category, "INSERT");
            if (invalidData != null) {
                response = new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(), invalidData, null));
            } else {
                Category findCategoryByCode = categoryService.findByCode(category.getCode());
                if (findCategoryByCode != null) {
                    response = new ResponseMessage(HttpStatus.CONFLICT.value(), "Mã thể loại đã tồn tại",
                            new MessageContent(HttpStatus.CONFLICT.value(), "Mã thể loại đã tồn tại", null));
                } else {
                    category.setUuid(UUID.randomUUID().toString());
                    // save data to database
                    categoryService.save(category);
                    // save data to elasticsearch
                    categoryEsService.save(category);

                    response = new ResponseMessage(HttpStatus.OK.value(), "Thêm mới thể loại thành công",
                            new MessageContent(HttpStatus.OK.value(), "Thêm mới thể loại thành công", category));
                }
            }
        }
        return response;
    }

    private Category setCategoryFromBodyParam(Map<String, Object> bodyParam) {
        String name = (String) bodyParam.get("name");
        String code = (String) bodyParam.get("code");
        Category category = new Category();
        category.setUuid(StringUtil.randomUUID());
        category.setName(name);
        category.setCode(code);
        return category;
    }

    // update category by uuid
    public ResponseMessage updateCategoryById(Map<String, Object> bodyParam, Map<String, String> headerParam,
                                              String pathParam, String method, String requestPath) throws ValidationException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            Category category = setCategoryFromBodyParam(bodyParam);
            category.setUuid(pathParam);
            String invalidData = new CategoryValidation().validateUpsert(category, "UPDATE");
            if (invalidData != null) {
                response = new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(), invalidData, null));
            } else {
                Category categoryById = categoryService.findByUuid(pathParam);

                if (categoryById == null) {
                    response = new ResponseMessage(HttpStatus.NOT_FOUND.value(), "Không tìm thấy thể loại với uuid " + pathParam,
                            new MessageContent(HttpStatus.NOT_FOUND.value(), "Không tìm thấy thể loại với uuid " + pathParam, null));
                } else {
                    categoryById = categoryService.findByCode(category.getCode());
                    if (categoryById != null && !categoryById.getUuid().equals(pathParam)){
                        response = new ResponseMessage(HttpStatus.CONFLICT.value(), "Mã thể loại đã tồn tại",
                                new MessageContent(HttpStatus.CONFLICT.value(), "Mã thể loại đã tồn tại", null));
                    } else {
                        categoryService.save(category);
                        // save data to elasticsearch
                        categoryEsService.save(category);
                        response = new ResponseMessage(HttpStatus.OK.value(), "Cập nhật thể loại thành công",
                                new MessageContent(HttpStatus.OK.value(), "Cập nhật thể loại thành công", category));
                    }
                }
            }
        }
        return response;
    }

    // delete category by uuid
    public ResponseMessage deleteCategoryById(String requestPath, Map<String, String> headerParam, String pathParam, String requestMethod) {
        ResponseMessage response = null;

        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            String uuid = pathParam;
            if (uuid == null) {
                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Uuid là bắt buộc",
                        new MessageContent(HttpStatus.BAD_REQUEST.value(), "Uuid là bắt buộc", null));
            } else {
                Category category = categoryService.findByUuid(uuid);
                if (category == null) {
                    response = new ResponseMessage(HttpStatus.NOT_FOUND.value(), "Không tìm thấy thể loại với uuid " + uuid,
                            new MessageContent(HttpStatus.NOT_FOUND.value(), "Không tìm thấy thể loại với uuid " + uuid, null));
                } else {
                    categoryService.delete(uuid);
                    // save data to elasticsearch
                    categoryEsService.delete(category);
                    response = new ResponseMessage(HttpStatus.OK.value(), "Xóa thể loại thành công",
                            new MessageContent(HttpStatus.OK.value(), "Xóa thể loại thành công", null));
                }
            }
        }
        return response;
    }

}
