package com.elcom.bookloan.service.library.impl;

import com.elcom.bookloan.model.library.Book;
import com.elcom.bookloan.repository.library.BookRepository;
import com.elcom.bookloan.service.library.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public Book findByUuid(String uuid) {
        Optional<Book> book = bookRepository.findById(uuid);
        if(book.isPresent())
            return book.get();
        return null;
    }
}
