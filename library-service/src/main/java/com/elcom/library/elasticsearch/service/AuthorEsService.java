package com.elcom.library.elasticsearch.service;

import com.elcom.library.elasticsearch.repository.AuthorEsRepository;
import com.elcom.library.model.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorEsService {

    @Autowired
    private AuthorEsRepository authorEsRepository;

    public void save(Author author){
        authorEsRepository.save(author);
    }
}
