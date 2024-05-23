package com.elcom.id.controller;

import com.elcom.id.constant.Constant;
import com.elcom.id.model.Student;
import com.elcom.id.model.dto.AuthorizationResponseDTO;
import com.elcom.id.service.StudentService;
import com.elcom.id.validation.StudentValidation;
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

@Controller
public class StudentController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenController.class);

    @Autowired
    private StudentService studentService;

    // get all students
    public ResponseMessage getAllStudents(String requestUrl, String method, String urlParam, Map<String, String> headerParam) {
        ResponseMessage response = null;

        AuthorizationResponseDTO dto = getAuthorFromToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            response = new ResponseMessage(HttpStatus.OK.value(), "Lấy danh sách học sinh",
                    new MessageContent(HttpStatus.OK.value(), "Lấy danh sách học sinh", studentService.getAllStudents()));
        }

        return response;
    }

    // create new student
    public ResponseMessage createStudent(String requestUrl, String method, Map<String, String> headerParam,
                                         Map<String, Object> bodyParam) throws ValidationException {
        ResponseMessage response = null;

        AuthorizationResponseDTO dto = getAuthorFromToken(headerParam);

        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            Student student = setStudentFromBodyParam(bodyParam);
            String invalidData = new StudentValidation().validateUpsert(student, "INSERT");
            if (invalidData != null) {
                response = new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(), invalidData, null));
            } else {
                student.setUuid(StringUtil.randomUUID());
                studentService.save(student);

                response = new ResponseMessage(HttpStatus.OK.value(), "Thêm mới học sinh thành công",
                        new MessageContent(HttpStatus.OK.value(), "Thêm mới học sinh thành công", student));
            }
        }

        return response;
    }

    // update student by uuid
    public ResponseMessage updateStudent(Map<String, Object> bodyParam, Map<String, String> headerParam,
                                         String pathParam, String method, String requestPath) throws Exception {
        ResponseMessage response = null;

        AuthorizationResponseDTO dto = getAuthorFromToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            Student student = setStudentFromBodyParam(bodyParam);
            student.setUuid(pathParam);
            String invalidData = new StudentValidation().validateUpsert(student, "UPDATE");
            if (invalidData != null) {
                response = new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(), invalidData, null));
            } else {
                Student studentFindById = studentService.findStudentByUuid(pathParam);
                if (studentFindById == null) {
                    String message = "Không tìm thấy học sinh với uuid: " + pathParam;
                    response = new ResponseMessage(HttpStatus.NOT_FOUND.value(), message,
                            new MessageContent(HttpStatus.NOT_FOUND.value(), message, null));
                } else {
                    studentService.save(student);
                    response = new ResponseMessage(HttpStatus.OK.value(), "Cập nhật học sinh thành công",
                            new MessageContent(HttpStatus.OK.value(), "Cập nhật học sinh thành công", student));
                }
            }
        }

        return response;
    }

    private Student setStudentFromBodyParam(Map<String, Object> bodyParam) {
        String lastName = (String) bodyParam.get("lastName");
        String firstName = (String) bodyParam.get("firstName");
        String phoneNumber = (String) bodyParam.get("phoneNumber");
        String email = (String) bodyParam.get("email");
        String address = (String) bodyParam.get("address");
        Integer gender = (Integer) bodyParam.get("gender");

        Student student = new Student();
        student.setLastName(lastName);
        student.setFirstName(firstName);
        student.setPhoneNumber(phoneNumber);
        student.setEmail(email);
        student.setAddress(address);
        student.setGender(gender);
        return student;
    }

    // delete student by uuid
    public ResponseMessage deleteStudent(String requestPath, Map<String, String> headerParam, String pathParam, String requestMethod) {
        ResponseMessage response = null;

        AuthorizationResponseDTO dto = getAuthorFromToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            String uuid = pathParam;
            if (uuid == null) {
                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                        new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
            } else {
                Student student = studentService.findStudentByUuid(uuid);
                if (student == null) {
                    response = new ResponseMessage(HttpStatus.NOT_FOUND.value(), "Không tìm thấy học sinh với uuid " + uuid,
                            new MessageContent(HttpStatus.NOT_FOUND.value(), "Không tìm thấy học sinh với uuid " + uuid, null));
                } else {
                    studentService.delete(uuid);
                    response = new ResponseMessage(HttpStatus.OK.value(), "Xóa học sinh thành công",
                            new MessageContent(HttpStatus.OK.value(), "Xóa học sinh thành công", null));
                }
            }
        }

        return response;
    }

}
