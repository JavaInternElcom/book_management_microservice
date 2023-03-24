package com.elcom.library.elasticsearch.service;

import com.elcom.library.elasticsearch.repository.CategoryEsRepository;
import com.elcom.library.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryEsService {

    @Autowired
    private CategoryEsRepository categoryEsRepository;

    public void save(Category category){
        categoryEsRepository.save(category);
    }

    public void delete(Category category) {
        categoryEsRepository.delete(category);
    }
}
