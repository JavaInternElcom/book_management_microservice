package com.elcom.id.controller;

import com.demo.grpc.User;
import com.elcom.id.model.dto.UserDTO;
import com.elcom.id.service.UserClientService;
import com.elcom.message.MessageContent;
import com.elcom.message.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

import javax.xml.bind.ValidationException;
import java.util.Map;

@Controller
public class UserController {

    @Autowired
    private UserClientService userClientService;

    public ResponseMessage createUser(String requestUrl, String method, Map<String, String> headerParam,
                                      Map<String, Object> bodyParam) throws ValidationException {
        ResponseMessage response = null;

//        AuthorizationResponseDTO dto = getAuthorFromToken(headerParam);
//
//        if (dto == null) {
//            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
//                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
//        } else {
//            Student student = setStudentFromBodyParam(bodyParam);
//            String invalidData = new StudentValidation().validateUpsert(student, "INSERT");
//            if (invalidData != null) {
//                response = new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(), invalidData, null));
//            } else {
        User user = userClientService.createUser((String) bodyParam.get("username"), (String) bodyParam.get("password"));
        UserDTO userDTO = transform(user);

        response = new ResponseMessage(HttpStatus.OK.value(), "Thêm mới người dùng thành công",
                new MessageContent(HttpStatus.OK.value(), "Thêm mới người dùng thành công", userDTO));
//            }
//        }

        return response;
    }

    private UserDTO transform(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());
        return userDTO;
    }
}
