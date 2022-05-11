package com.elcom.search.controller;

import com.elcom.message.MessageContent;
import com.elcom.message.ResponseMessage;
import com.elcom.search.elasticsearch.service.BookEsService;
import com.elcom.search.model.dto.AuthorizationResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
public class SearchController extends BaseController{

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchController.class);

    @Autowired
    private BookEsService bookEsService;

    // find book by name using elasticsearch
    public ResponseMessage findBookByName(Map<String, String> headerParam, String requestPath,
                                          String requestMethod, String pathParam){
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if(dto == null){
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "You are not logged in",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "You are not logged in", null));
        } else {
            String name = pathParam;
            response = new ResponseMessage(HttpStatus.OK.value(), "Get all book by name " + name,
                    new MessageContent(HttpStatus.OK.value(), "Get all book by name " + name, bookEsService.findByName(name)));
        }
        return response;
    }
}
