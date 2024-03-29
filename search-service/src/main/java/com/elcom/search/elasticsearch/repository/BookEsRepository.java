package com.elcom.search.elasticsearch.repository;

import com.elcom.search.model.Book;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookEsRepository extends ElasticsearchRepository<Book, String> {

    List<Book> findByNameLike(String name);

    List<Book> findByNameContainingIgnoreCase(String name);
}