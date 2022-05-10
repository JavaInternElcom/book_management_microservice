package com.elcom.library.elasticsearch.repository;

import com.elcom.library.model.Book;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookEsRepository extends ElasticsearchRepository<Book, String> {
    List<Book> findByName(String name);
}
