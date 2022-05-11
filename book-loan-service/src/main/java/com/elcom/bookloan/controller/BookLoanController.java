package com.elcom.bookloan.controller;

import com.elcom.bookloan.model.dto.AuthorizationResponseDTO;
import com.elcom.bookloan.model.library.Book;
import com.elcom.bookloan.model.library.Student;
import com.elcom.bookloan.model.log.BookLoan;
import com.elcom.bookloan.service.library.BookService;
import com.elcom.bookloan.service.library.StudentService;
import com.elcom.bookloan.service.log.BookLoanService;
import com.elcom.message.MessageContent;
import com.elcom.message.ResponseMessage;
import com.elcom.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

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
                                      Map<String, Object> bodyParam){
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            String studentId = (String) bodyParam.get("studentId");
            String bookId = (String) bodyParam.get("bookId");

            // check book exist
            Book book = bookService.findByUuid(bookId);
            if(book == null){
                response = new ResponseMessage(HttpStatus.NOT_FOUND.value(), "Not found book with uuid " + bookId,
                        new MessageContent(HttpStatus.NOT_FOUND.value(), "Not found book with uuid " + bookId, null));
            } else {
                // check student exist
                Student student = studentService.findByUuid(studentId);
                if(student == null){
                    response = new ResponseMessage(HttpStatus.NOT_FOUND.value(), "Not found student with uuid " + studentId,
                            new MessageContent(HttpStatus.NOT_FOUND.value(), "Not found student with uuid " + studentId, null));
                }

                else{
                    BookLoan bookLoan = new BookLoan();
                    bookLoan.setUuid(StringUtil.randomUUID());
                    bookLoan.setBookName(book.getName());
                    bookLoan.setBookId(bookId);
                    bookLoan.setStudentId(studentId);
                    bookLoan.setBorrowDate(new Date());
                    bookLoan.setStatus(1);
                    bookLoanService.save(bookLoan);
                    response = new ResponseMessage(HttpStatus.OK.value(), "Save log borrow book successfully.",
                            new MessageContent(HttpStatus.OK.value(), "Save log borrow book successfully.", bookLoan));
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
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "You are not logged in.",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "You are not logged in", null));
        } else {
            String uuid = pathParam;
            if (uuid == null) {
                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Id is required in requestParam",
                        new MessageContent(HttpStatus.BAD_REQUEST.value(), "Id is required in requestParam", null));
            } else {
                BookLoan bookLoan = bookLoanService.findBookLoanUnpaid(uuid);
                if(bookLoan == null){
                    response = new ResponseMessage(HttpStatus.NOT_FOUND.value(), "Not found book loan with uuid " + uuid,
                            new MessageContent(HttpStatus.NOT_FOUND.value(), "Not found book loan with uuid " + uuid, null));
                } else {
                    bookLoan.setStatus(0);
                    bookLoanService.save(bookLoan);
                    response = new ResponseMessage(HttpStatus.OK.value(), "Delete book loan successfully.",
                            new MessageContent(HttpStatus.OK.value(), "Delete book loan successfully.", null));
                }
            }
        }
        return response;
    }
}
