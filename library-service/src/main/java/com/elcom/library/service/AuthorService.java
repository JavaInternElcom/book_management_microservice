package com.elcom.library.service;

import com.elcom.library.model.Author;

import java.util.List;

public interface AuthorService {

    List<Author> findAll();

    Author findByUuid(String uuid);

    void save(Author author);

    void delete(String uuid);
}
