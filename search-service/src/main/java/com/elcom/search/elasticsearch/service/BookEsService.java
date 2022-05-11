package com.elcom.search.elasticsearch.service;

import com.elcom.search.model.Book;

import java.util.List;

public interface BookEsService {

    List<Book> findByName(String name);
}
