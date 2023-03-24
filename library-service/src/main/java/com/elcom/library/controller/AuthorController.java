package com.elcom.library.controller;

import com.elcom.library.elasticsearch.service.AuthorEsService;
import com.elcom.library.model.Author;
import com.elcom.library.model.dto.AuthorizationResponseDTO;
import com.elcom.library.service.AuthorService;
import com.elcom.library.validation.AuthorValidation;
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
public class AuthorController extends BaseController{

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorController.class);

    @Autowired
    private AuthorService authorService;

    @Autowired
    private AuthorEsService authorEsService;

    // get all authors
    public ResponseMessage getAllAuthors(Map<String, String> headerParam, String requestPath,
                                            String requestMethod, String urlParam){

        long start = System.currentTimeMillis();
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        long end = System.currentTimeMillis();
        LOGGER.info("getAllAuthors >>> authenToken in {} ms", (end - start));
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            response = new ResponseMessage(HttpStatus.OK.value(), "Lấy danh sách tác giả",
                    new MessageContent(HttpStatus.OK.value(), "Lấy danh sách tác giả", authorService.findAll()));
        }

        return response;
    }

    // create new author
    public ResponseMessage createAuthor(String requestPath, String method, Map<String, String> headerParam,
                                          Map<String, Object> bodyParam) throws ValidationException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            Author author = setAuthorFromBodyParam(bodyParam);
            String invalidData = new AuthorValidation().validateUpsert(author, "INSERT");
            if (invalidData != null) {
                response = new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(), invalidData, null));
            } else {
                author.setUuid(UUID.randomUUID().toString());
                // save data to database
                authorService.save(author);
                // save data to elasticsearch
                authorEsService.save(author);

                response = new ResponseMessage(HttpStatus.OK.value(), "Thêm mới tác giả thành công",
                        new MessageContent(HttpStatus.OK.value(), "Thêm mới tác giả thành công", author));
            }
        }
        return response;
    }

    private Author setAuthorFromBodyParam(Map<String, Object> bodyParam) {
        String lastName = (String) bodyParam.get("lastName");
        String firstName = (String) bodyParam.get("firstName");
        String phoneNumber = (String) bodyParam.get("phoneNumber");
        String email = (String) bodyParam.get("email");
        String address = (String) bodyParam.get("address");

        Author author = new Author();
        author.setLastName(lastName);
        author.setFirstName(firstName);
        author.setPhoneNumber(phoneNumber);
        author.setEmail(email);
        author.setAddress(address);
        return author;
    }

    // update author by uuid
    public ResponseMessage updateAuthorById(Map<String, Object> bodyParam, Map<String, String> headerParam,
                                              String pathParam, String method, String requestPath) throws ValidationException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            Author author = setAuthorFromBodyParam(bodyParam);
            author.setUuid(pathParam);
            String invalidData = new AuthorValidation().validateUpsert(author, "UPDATE");
            if (invalidData != null) {
                response = new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(), invalidData, null));
            } else {
                Author authorById = authorService.findByUuid(pathParam);

                if(authorById == null){
                    response = new ResponseMessage(HttpStatus.NOT_FOUND.value(), "Không tìm thấy tác giả với uuid " + pathParam,
                            new MessageContent(HttpStatus.NOT_FOUND.value(), "Không tìm thấy tác giả với uuid " + pathParam, null));
                } else {
                    authorService.save(author);
                    // save data to elasticsearch
                    authorEsService.save(author);

                    response = new ResponseMessage(HttpStatus.OK.value(), "Cập nhật tác giả thành công",
                            new MessageContent(HttpStatus.OK.value(), "Cập nhật tác giả thành công", author));
                }
            }
        }
        return response;
    }

    // delete author by uuid
    public ResponseMessage deleteAuthorById(String requestPath, Map<String, String> headerParam, String pathParam, String requestMethod){
        ResponseMessage response = null;

        AuthorizationResponseDTO dto = authenToken(headerParam);
        if(dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            String uuid = pathParam;
            if (uuid == null) {
                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Uuid là bắt buộc",
                        new MessageContent(HttpStatus.BAD_REQUEST.value(), "Uuid là bắt buộc", null));
            } else {
                Author author = authorService.findByUuid(uuid);
                if(author == null){
                    response = new ResponseMessage(HttpStatus.NOT_FOUND.value(), "Không tìm thấy tác giả với uuid " + uuid,
                            new MessageContent(HttpStatus.NOT_FOUND.value(), "Không tìm thấy tác giả với uuid " + uuid, null));
                } else {
                    authorService.delete(uuid);
                    authorEsService.delete(author);
                    response = new ResponseMessage(HttpStatus.OK.value(), "Xóa tác giả thành công",
                            new MessageContent(HttpStatus.OK.value(), "Xóa tác giả thành công", null));
                }
            }
        }
        return response;
    }

}
