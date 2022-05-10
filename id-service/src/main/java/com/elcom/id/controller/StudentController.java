package com.elcom.id.controller;

import com.elcom.id.messaging.rabbitmq.model.Student;
import com.elcom.id.messaging.rabbitmq.model.dto.AuthorizationResponseDTO;
import com.elcom.id.service.StudentService;
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
public class StudentController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenController.class);

    @Autowired
    private StudentService studentService;

    // get all students
    public ResponseMessage getAllStudents(String requestUrl, String method, String urlParam, Map<String, String> headerParam){
        ResponseMessage response = null;

        AuthorizationResponseDTO dto = getAuthorFromToken(headerParam);
        if(dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "You are not logged in.",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "You are not logged in", null));
        } else {
            response = new ResponseMessage(HttpStatus.OK.value(), "Get all students",
                    new MessageContent(HttpStatus.OK.value(), "Get all students", studentService.getAllStudents()));
        }

        return response;
    }

    // create new student
    public ResponseMessage createStudent(String requestUrl, String method, Map<String, String> headerParam,
                                         Map<String, Object> bodyParam){
        ResponseMessage response = null;

        AuthorizationResponseDTO dto = getAuthorFromToken(headerParam);

        if(dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "You are not logged in.",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "You are not logged in", null));
        } else {
            String lastName = (String) bodyParam.get("lastName");
            String firstName = (String) bodyParam.get("firstName");
            String phoneNumber = (String) bodyParam.get("phoneNumber");
            String email = (String) bodyParam.get("email");
            String address = (String) bodyParam.get("address");
            Integer gender = (Integer) bodyParam.get("gender");

            Student student = new Student();
            student.setUuid(StringUtil.randomUUID());
            student.setLastName(lastName);
            student.setFirstName(firstName);
            student.setPhoneNumber(phoneNumber);
            student.setEmail(email);
            student.setAddress(address);
            student.setGender(gender);

            studentService.save(student);

            response = new ResponseMessage(HttpStatus.OK.value(), "Create new student successfully.",
                    new MessageContent(HttpStatus.OK.value(), "Create new student successfully.", student));
        }

        return response;
    }

    // update student by uuid
    public ResponseMessage updateStudent(Map<String, Object> bodyParam, Map<String, String> headerParam,
                                         String pathParam, String method, String requestPath) throws Exception {
        ResponseMessage response = null;

        AuthorizationResponseDTO dto = getAuthorFromToken(headerParam);
        if(dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "You are not logged in.",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "You are not logged in", null));
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
                Integer gender = (Integer) bodyParam.get("gender");

                Student student = studentService.findStudentByUuid(uuid);

                if(student == null){
                    String message = "Student not found with uuid: " + uuid;
                    response = new ResponseMessage(HttpStatus.NOT_FOUND.value(), message,
                            new MessageContent(HttpStatus.NOT_FOUND.value(), message, null));
                } else {
                    student.setUuid(uuid);
                    student.setLastName(lastName);
                    student.setFirstName(firstName);
                    student.setPhoneNumber(phoneNumber);
                    student.setEmail(email);
                    student.setAddress(address);
                    student.setGender(gender);

                    studentService.save(student);

                    response = new ResponseMessage(HttpStatus.OK.value(), "Update student successfully.",
                            new MessageContent(HttpStatus.OK.value(), "Update student successfully.", student));
                }

            }
        }

        return response;
    }

    // delete student by uuid
    public ResponseMessage deleteStudent(String requestPath, Map<String, String> headerParam, String pathParam, String requestMethod){
        ResponseMessage response = null;

        AuthorizationResponseDTO dto = getAuthorFromToken(headerParam);
        if(dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "You are not logged in.",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "You are not logged in", null));
        } else {
            String uuid = pathParam;
            if(uuid == null){
                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Id is required in requestParam",
                        new MessageContent(HttpStatus.BAD_REQUEST.value(), "Id is required in requestParam", null));
            } else {
                Student student = studentService.findStudentByUuid(uuid);
                if(student == null){
                    response = new ResponseMessage(HttpStatus.NOT_FOUND.value(), "Not found student with uuid " + uuid,
                            new MessageContent(HttpStatus.NOT_FOUND.value(), "Not found student with uuid " + uuid, null));
                } else {
                    studentService.delete(uuid);
                    response = new ResponseMessage(HttpStatus.OK.value(), "Delete student successfully.",
                            new MessageContent(HttpStatus.OK.value(), "Delete student successfully.", null));
                }
            }
        }

        return response;
    }

}
