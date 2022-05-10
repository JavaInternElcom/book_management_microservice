package com.elcom.library.service.impl;

import com.elcom.library.model.Book;
import com.elcom.library.repository.BookRepository;
import com.elcom.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public void save(Book book) {
        bookRepository.save(book);
    }

    @Override
    public Book findByUuid(String uuid) {
        Optional<Book> book = bookRepository.findById(uuid);
        if(book.isPresent())
            return book.get();
        return null;
    }

    @Override
    public void delete(String uuid) {
        bookRepository.deleteById(uuid);
    }
}
