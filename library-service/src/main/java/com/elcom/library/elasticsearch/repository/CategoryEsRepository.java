package com.elcom.library.elasticsearch.repository;

import com.elcom.library.model.Category;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryEsRepository extends ElasticsearchRepository<Category, String> {
}
