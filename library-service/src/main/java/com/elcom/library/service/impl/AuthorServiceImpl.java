package com.elcom.library.service.impl;

import com.elcom.library.model.Author;
import com.elcom.library.repository.AuthorRepository;
import com.elcom.library.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Override
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @Override
    public Author findByUuid(String uuid) {
        Optional<Author> author = authorRepository.findById(uuid);
        if(author.isPresent())
            return author.get();
        return null;
    }

    @Override
    public void save(Author author) {
        authorRepository.save(author);
    }

    @Override
    public void delete(String uuid) {
        authorRepository.deleteById(uuid);
    }
}
