package com.elcom.library.controller;

import com.elcom.library.elasticsearch.service.BookEsService;
import com.elcom.library.model.Author;
import com.elcom.library.model.Book;
import com.elcom.library.model.BookDTO;
import com.elcom.library.model.Category;
import com.elcom.library.model.dto.AuthorizationResponseDTO;
import com.elcom.library.service.AuthorService;
import com.elcom.library.service.BookService;
import com.elcom.library.service.CategoryService;
import com.elcom.library.validation.BookValidation;
import com.elcom.message.MessageContent;
import com.elcom.message.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

import javax.xml.bind.ValidationException;
import java.util.Map;
import java.util.UUID;

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
                                       String requestMethod, String urlParam) {

        long start = System.currentTimeMillis();
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        long end = System.currentTimeMillis();
        LOGGER.info("getAllcategorys >>> authenToken in {} ms", (end - start));
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            response = new ResponseMessage(HttpStatus.OK.value(), "Lấy danh sách sách",
                    new MessageContent(HttpStatus.OK.value(), "Lấy danh sách sách", bookService.findAll()));
        }

        return response;
    }

    // create new book
    public ResponseMessage createBook(String requestPath, String method, Map<String, String> headerParam,
                                      Map<String, Object> bodyParam) throws ValidationException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            BookDTO bookMapped = setBookFromBodyParam(bodyParam);
            String invalidData = new BookValidation().validateUpsert(bookMapped, "INSERT");
            if (invalidData != null) {
                response = new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(), invalidData, null));
            } else {
                // check category exist
                String categoryId = (String) bodyParam.get("categoryId");
                Category findCategoryByUuid = categoryService.findByUuid(categoryId);
                if (findCategoryByUuid == null) {
                    response = new ResponseMessage(HttpStatus.NOT_FOUND.value(), "Không tìm thấy thể loại với uuid " + categoryId,
                            new MessageContent(HttpStatus.NOT_FOUND.value(), "Không tìm thấy thể loại với uuid " + categoryId, null));
                } else {

                    // check author exist
                    String authorId = (String) bodyParam.get("authorId");
                    Author findAuthorByUuid = authorService.findByUuid(authorId);
                    if (findAuthorByUuid == null) {
                        response = new ResponseMessage(HttpStatus.NOT_FOUND.value(), "Không tìm thấy tác giả với uuid " + authorId,
                                new MessageContent(HttpStatus.NOT_FOUND.value(), "Không tìm thấy tác giả với uuid " + authorId, null));
                    } else {
                        String name = (String) bodyParam.get("name");

                        Book book = new Book();
                        book.setUuid(UUID.randomUUID().toString());
                        book.setName(name);
                        book.setFirstLetter(String.valueOf(name.charAt(0)));

                        // save to elasticsearch
                        bookEsService.save(book);

                        book.setAuthor(findAuthorByUuid);
                        book.setCategory(findCategoryByUuid);

                        // save to database
                        bookService.save(book);

                        response = new ResponseMessage(HttpStatus.OK.value(), "Thêm mới sách thành công",
                                new MessageContent(HttpStatus.OK.value(), "Thêm mới sách thành công", book));
                    }
                }
            }
        }
        return response;
    }

    private BookDTO setBookFromBodyParam(Map<String, Object> bodyParam) {
        BookDTO book = new BookDTO();
        book.setName((String) bodyParam.get("name"));
        book.setCategoryId((String) bodyParam.get("categoryId"));
        book.setAuthorId((String) bodyParam.get("authorId"));
        return book;
    }

    // update book by uuid
    public ResponseMessage updateBookById(Map<String, Object> bodyParam, Map<String, String> headerParam,
                                          String pathParam, String method, String requestPath) throws ValidationException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            BookDTO bookMapped = setBookFromBodyParam(bodyParam);
            bookMapped.setUuid(pathParam);
            String invalidData = new BookValidation().validateUpsert(bookMapped, "UPDATE");
            if (invalidData != null) {
                response = new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(), invalidData, null));
            } else {
                // check book exist
                Book findBookById = bookService.findByUuid(pathParam);

                if (findBookById == null) {
                    response = new ResponseMessage(HttpStatus.NOT_FOUND.value(), "Không tìm thấy sách với  uuid " + pathParam,
                            new MessageContent(HttpStatus.NOT_FOUND.value(), "Không tìm thấy sách với  uuid " + pathParam, null));
                } else {
                    // check category exist
                    String categoryId = (String) bodyParam.get("categoryId");
                    Category findCategoryByUuid = categoryService.findByUuid(categoryId);
                    if (findCategoryByUuid == null) {
                        response = new ResponseMessage(HttpStatus.NOT_FOUND.value(), "Không tìm thấy thể loại với uuid " + categoryId,
                                new MessageContent(HttpStatus.NOT_FOUND.value(), "Không tìm thấy thể loại với uuid " + categoryId, null));
                    } else {
                        // check author exist
                        String authorId = (String) bodyParam.get("authorId");
                        Author findAuthorByUuid = authorService.findByUuid(authorId);
                        if (findAuthorByUuid == null) {
                            response = new ResponseMessage(HttpStatus.NOT_FOUND.value(), "Không tìm thấy tác giả với uuid " + authorId,
                                    new MessageContent(HttpStatus.NOT_FOUND.value(), "Không tìm thấy tác giả với uuid " + authorId, null));
                        } else {
                            String name = (String) bodyParam.get("name");

                            Book book = new Book();
                            book.setUuid(pathParam);
                            book.setName(name);
                            book.setFirstLetter(String.valueOf(name.charAt(0)));

                            // save to elasticsearch
                            bookEsService.save(book);

                            book.setAuthor(findAuthorByUuid);
                            book.setCategory(findCategoryByUuid);

                            // save to database
                            bookService.save(book);

                            response = new ResponseMessage(HttpStatus.OK.value(), "Cập nhật sách thành công",
                                    new MessageContent(HttpStatus.OK.value(), "Cập nhật sách thành công", book));
                        }
                    }
                }
            }
        }
        return response;
    }

    // delete book by uuid
    public ResponseMessage deleteBookById(String requestPath, Map<String, String> headerParam, String pathParam, String requestMethod) {
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
                Book book = bookService.findByUuid(uuid);
                if (book == null) {
                    response = new ResponseMessage(HttpStatus.NOT_FOUND.value(), "Không tìm thấy sách với uuid " + uuid,
                            new MessageContent(HttpStatus.NOT_FOUND.value(), "Không tìm thấy sách với uuid " + uuid, null));
                } else {
                    Book bookInEs = new Book();
                    bookInEs.setUuid(book.getUuid());
                    bookInEs.setName(book.getName());
                    bookInEs.setFirstLetter(book.getFirstLetter());
                    bookEsService.delete(bookInEs);
                    bookService.delete(uuid);
                    response = new ResponseMessage(HttpStatus.OK.value(), "Xóa sách thành công",
                            new MessageContent(HttpStatus.OK.value(), "Xóa sách thành công", null));
                }
            }
        }
        return response;
    }
}
