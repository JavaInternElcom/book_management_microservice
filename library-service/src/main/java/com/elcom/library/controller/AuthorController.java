package com.elcom.library.controller;

import com.elcom.library.elasticsearch.service.AuthorEsService;
import com.elcom.library.model.Author;
import com.elcom.library.model.dto.AuthorizationResponseDTO;
import com.elcom.library.service.AuthorService;
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
            response = new ResponseMessage(HttpStatus.OK.value(), "Get all authors successfully.",
                    new MessageContent(HttpStatus.OK.value(), "Get all authors successfully.", authorService.findAll()));
        }

        return response;
    }

    // create new author
    public ResponseMessage createAuthor(String requestPath, String method, Map<String, String> headerParam,
                                          Map<String, Object> bodyParam){
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            String lastName = (String) bodyParam.get("lastName");
            String firstName = (String) bodyParam.get("firstName");
            String phoneNumber = (String) bodyParam.get("phoneNumber");
            String email = (String) bodyParam.get("email");
            String address = (String) bodyParam.get("address");
            
            Author author = new Author();
            author.setUuid(StringUtil.randomUUID());
            author.setLastName(lastName);
            author.setFirstName(firstName);
            author.setPhoneNumber(phoneNumber);
            author.setEmail(email);
            author.setAddress(address);

            // save data to database
            authorService.save(author);
            // save data to elasticsearch
            authorEsService.save(author);

            response = new ResponseMessage(HttpStatus.OK.value(), "Create new author successfully.",
                    new MessageContent(HttpStatus.OK.value(), "Create new author successfully.", author));
        }
        return response;
    }

    // update author by uuid
    public ResponseMessage updateAuthorById(Map<String, Object> bodyParam, Map<String, String> headerParam,
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
                String lastName = (String) bodyParam.get("lastName");
                String firstName = (String) bodyParam.get("firstName");
                String phoneNumber = (String) bodyParam.get("phoneNumber");
                String email = (String) bodyParam.get("email");
                String address = (String) bodyParam.get("address");

                Author author = authorService.findByUuid(uuid);

                if(author == null){
                    response = new ResponseMessage(HttpStatus.NOT_FOUND.value(), "Not found author with uuid " + uuid,
                            new MessageContent(HttpStatus.NOT_FOUND.value(), "Not found author with uuid " + uuid, null));
                } else {
                    author.setUuid(uuid);
                    author.setAddress(address);
                    author.setEmail(email);
                    author.setPhoneNumber(phoneNumber);
                    author.setFirstName(firstName);
                    author.setLastName(lastName);

                    authorService.save(author);

                    response = new ResponseMessage(HttpStatus.OK.value(), "Update author successfully.",
                            new MessageContent(HttpStatus.OK.value(), "Update author successfully.", author));
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
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "You are not logged in.",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "You are not logged in", null));
        } else {
            String uuid = pathParam;
            if (uuid == null) {
                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Id is required in requestParam",
                        new MessageContent(HttpStatus.BAD_REQUEST.value(), "Id is required in requestParam", null));
            } else {
                Author author = authorService.findByUuid(uuid);
                if(author == null){
                    response = new ResponseMessage(HttpStatus.NOT_FOUND.value(), "Not found author with uuid " + uuid,
                            new MessageContent(HttpStatus.NOT_FOUND.value(), "Not found author with uuid " + uuid, null));
                } else {
                    authorService.delete(uuid);
                    response = new ResponseMessage(HttpStatus.OK.value(), "Delete author successfully.",
                            new MessageContent(HttpStatus.OK.value(), "Delete author successfully.", null));
                }
            }
        }
        return response;
    }

}
