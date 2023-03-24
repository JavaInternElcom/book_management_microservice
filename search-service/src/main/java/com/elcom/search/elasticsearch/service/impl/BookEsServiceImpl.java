package com.elcom.search.elasticsearch.service.impl;

import com.elcom.search.elasticsearch.repository.BookEsRepository;
import com.elcom.search.elasticsearch.service.BookEsService;
import com.elcom.search.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookEsServiceImpl implements BookEsService {

    @Autowired
    private BookEsRepository bookEsRepository;

    public List<Book> findByName(String name){
        return bookEsRepository.findByNameContainingIgnoreCase(name);
    }
}
