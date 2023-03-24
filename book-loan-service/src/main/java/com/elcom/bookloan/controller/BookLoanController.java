package com.elcom.bookloan.controller;

import com.elcom.bookloan.model.dto.AuthorizationResponseDTO;
import com.elcom.bookloan.model.library.Book;
import com.elcom.bookloan.model.library.Student;
import com.elcom.bookloan.model.log.BookLoan;
import com.elcom.bookloan.service.library.BookService;
import com.elcom.bookloan.service.library.StudentService;
import com.elcom.bookloan.service.log.BookLoanService;
import com.elcom.bookloan.validation.BookLoanValidation;
import com.elcom.message.MessageContent;
import com.elcom.message.ResponseMessage;
import com.elcom.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

import javax.xml.bind.ValidationException;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Controller
public class BookLoanController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookLoanController.class);

    @Autowired
    private BookService bookService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private BookLoanService bookLoanService;

    // save log borrow book
    public ResponseMessage borrowBook(String requestPath, String method, Map<String, String> headerParam,
                                      Map<String, Object> bodyParam) throws ValidationException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            String studentId = (String) bodyParam.get("studentId");
            String bookId = (String) bodyParam.get("bookId");
            String invalidData = new BookLoanValidation().validateBorrowLog(bookId, studentId);
            if(invalidData != null) {
                response = new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(), invalidData, null));
            }
            else {
                // check book exist
                Book book = bookService.findByUuid(bookId);
                if(book == null){
                    response = new ResponseMessage(HttpStatus.NOT_FOUND.value(), "Không tìm thấy sách với uuid " + bookId,
                            new MessageContent(HttpStatus.NOT_FOUND.value(), "Không tìm thấy sách với uuid " + bookId, null));
                } else {
                    // check student exist
                    Student student = studentService.findByUuid(studentId);
                    if(student == null){
                        response = new ResponseMessage(HttpStatus.NOT_FOUND.value(), "Không tìm thấy học sinh với uuid " + studentId,
                                new MessageContent(HttpStatus.NOT_FOUND.value(), "Không tìm thấy học sinh với uuid " + studentId, null));
                    }

                    else{
                        BookLoan bookLoan = new BookLoan();
                        bookLoan.setUuid(UUID.randomUUID().toString());
                        bookLoan.setBookName(book.getName());
                        bookLoan.setBookId(bookId);
                        bookLoan.setStudentId(studentId);
                        bookLoan.setBorrowDate(new Date());
                        bookLoan.setStatus(1);
                        bookLoanService.save(bookLoan);
                        response = new ResponseMessage(HttpStatus.OK.value(), "Mượn sách thành công",
                                new MessageContent(HttpStatus.OK.value(), "Mượn sách thành công", bookLoan));
                    }
                }
            }

        }
        return response;
    }

    // save log pay book
    public ResponseMessage payBook(String requestPath, Map<String, String> headerParam, String pathParam, String requestMethod){
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
                BookLoan bookLoan = bookLoanService.findBookLoanUnpaid(uuid);
                if(bookLoan == null){
                    response = new ResponseMessage(HttpStatus.NOT_FOUND.value(), "Không tìm thấy lịch sử mượn sách với uuid " + uuid,
                            new MessageContent(HttpStatus.NOT_FOUND.value(), "Không tìm thấy lịch sử mượn sách với uuid " + uuid, null));
                } else {
                    bookLoan.setStatus(0);
                    bookLoanService.save(bookLoan);
                    response = new ResponseMessage(HttpStatus.OK.value(), "Trả sách thành công",
                            new MessageContent(HttpStatus.OK.value(), "Trả sách thành công", null));
                }
            }
        }
        return response;
    }
}
