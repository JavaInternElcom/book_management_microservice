package com.elcom.library.service;

import com.elcom.library.model.Book;

import java.util.List;

public interface BookService {
    List<Book> findAll();

    void save(Book book);

    Book findByUuid(String uuid);

    void delete(String uuid);
}
