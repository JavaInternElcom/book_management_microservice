package com.elcom.library.elasticsearch.service;

import com.elcom.library.elasticsearch.repository.BookEsRepository;
import com.elcom.library.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookEsService {

    @Autowired
    private BookEsRepository bookEsRepository;

    public void save(Book book){
        bookEsRepository.save(book);
    }
}
