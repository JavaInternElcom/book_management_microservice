package com.elcom.library.controller;

import com.elcom.library.elasticsearch.service.BookEsService;
import com.elcom.library.model.Author;
import com.elcom.library.model.Book;
import com.elcom.library.model.Category;
import com.elcom.library.model.dto.AuthorizationResponseDTO;
import com.elcom.library.service.AuthorService;
import com.elcom.library.service.BookService;
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
public class BookController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookController.class);

    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private AuthorService authorService;
    
    @Autowired
    private BookService bookService;

    @Autowired
    private BookEsService bookEsService;

    // get all books
    public ResponseMessage getAllBooks(Map<String, String> headerParam, String requestPath,
                                         String requestMethod, String urlParam){

        long start = System.currentTimeMillis();
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        long end = System.currentTimeMillis();
        LOGGER.info("getAllcategorys >>> authenToken in {} ms", (end - start));
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            response = new ResponseMessage(HttpStatus.OK.value(), "Get all books successfully.",
                    new MessageContent(HttpStatus.OK.value(), "Get all books successfully.", bookService.findAll()));
        }

        return response;
    }

    // create new book
    public ResponseMessage createBook(String requestPath, String method, Map<String, String> headerParam,
                                        Map<String, Object> bodyParam){
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            // check category exist
            String categoryId = (String) bodyParam.get("categoryId");
            Category findCategoryByUuid = categoryService.findByUuid(categoryId);
            if(findCategoryByUuid == null){
                response = new ResponseMessage(HttpStatus.NOT_FOUND.value(), "Not found category with uuid " + categoryId,
                        new MessageContent(HttpStatus.NOT_FOUND.value(), "Not found category with uuid " + categoryId, null));
            }

            // check author exist
            String authorId = (String) bodyParam.get("authorId");
            Author findAuthorByUuid = authorService.findByUuid(authorId);
            if(findAuthorByUuid == null){
                response = new ResponseMessage(HttpStatus.NOT_FOUND.value(), "Not found author with uuid " + authorId,
                        new MessageContent(HttpStatus.NOT_FOUND.value(), "Not found author with uuid " + authorId, null));
            }

            else {
                String name = (String) bodyParam.get("name");

                Book book = new Book();
                book.setUuid(StringUtil.randomUUID());
                book.setName(name);
                book.setFirstLetter(String.valueOf(name.charAt(0)));

                // save to elasticsearch
                bookEsService.save(book);

                book.setAuthor(findAuthorByUuid);
                book.setCategory(findCategoryByUuid);

                // save to database
                bookService.save(book);

                response = new ResponseMessage(HttpStatus.OK.value(), "Create new book successfully.",
                        new MessageContent(HttpStatus.OK.value(), "Create new book successfully.", book));
            }
        }
        return response;
    }

    // update book by uuid
    public ResponseMessage updateBookById(Map<String, Object> bodyParam, Map<String, String> headerParam,
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
                // check book exist
                Book findBookById = bookService.findByUuid(uuid);

                if(findBookById == null){
                    response = new ResponseMessage(HttpStatus.NOT_FOUND.value(), "Not found book with uuid " + uuid,
                            new MessageContent(HttpStatus.NOT_FOUND.value(), "Not found book with uuid " + uuid, null));
                } else {
                    // check category exist
                    String categoryId = (String) bodyParam.get("categoryId");
                    Category findCategoryByUuid = categoryService.findByUuid(categoryId);
                    if(findCategoryByUuid == null){
                        response = new ResponseMessage(HttpStatus.NOT_FOUND.value(), "Not found category with uuid " + categoryId,
                                new MessageContent(HttpStatus.NOT_FOUND.value(), "Not found category with uuid " + categoryId, null));
                    }

                    // check author exist
                    String authorId = (String) bodyParam.get("authorId");
                    Author findAuthorByUuid = authorService.findByUuid(authorId);
                    if(findAuthorByUuid == null){
                        response = new ResponseMessage(HttpStatus.NOT_FOUND.value(), "Not found author with uuid " + authorId,
                                new MessageContent(HttpStatus.NOT_FOUND.value(), "Not found author with uuid " + authorId, null));
                    }

                    else {
                        String name = (String) bodyParam.get("name");

                        Book book = new Book();
                        book.setUuid(uuid);
                        book.setName(name);
                        book.setFirstLetter(String.valueOf(name.charAt(0)));

                        // save to elasticsearch
                        bookEsService.save(book);

                        book.setAuthor(findAuthorByUuid);
                        book.setCategory(findCategoryByUuid);

                        // save to database
                        bookService.save(book);

                        response = new ResponseMessage(HttpStatus.OK.value(), "Update book successfully.",
                                new MessageContent(HttpStatus.OK.value(), "Update book successfully.", book));
                    }
                }
            }
        }
        return response;
    }

    // delete book by uuid
    public ResponseMessage deleteBookById(String requestPath, Map<String, String> headerParam, String pathParam, String requestMethod){
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
                Book book = bookService.findByUuid(uuid);
                if(book == null){
                    response = new ResponseMessage(HttpStatus.NOT_FOUND.value(), "Not found book with uuid " + uuid,
                            new MessageContent(HttpStatus.NOT_FOUND.value(), "Not found book with uuid " + uuid, null));
                } else {
                    bookService.delete(uuid);
                    response = new ResponseMessage(HttpStatus.OK.value(), "Delete book successfully.",
                            new MessageContent(HttpStatus.OK.value(), "Delete book successfully.", null));
                }
            }
        }
        return response;
    }
}
