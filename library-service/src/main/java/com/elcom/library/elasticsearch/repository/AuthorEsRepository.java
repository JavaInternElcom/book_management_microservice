package com.elcom.library.elasticsearch.repository;

import com.elcom.library.model.Author;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorEsRepository extends ElasticsearchRepository<Author, String> {
}
