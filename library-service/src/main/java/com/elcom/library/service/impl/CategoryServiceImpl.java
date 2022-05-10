package com.elcom.library.service.impl;

import com.elcom.library.model.Category;
import com.elcom.library.repository.CategoryRepository;
import com.elcom.library.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public void save(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public Category findByUuid(String uuid) {
        Optional<Category> category = categoryRepository.findById(uuid);
        if(category.isPresent()){
            return category.get();
        }
        return null;
    }

    @Override
    public Category findByName(String name) {
        Optional<Category> category = categoryRepository.findByName(name);
        if(category.isPresent())
            return category.get();
        return null;
    }

    @Override
    public Category findByCode(String code) {
        Optional<Category> category = categoryRepository.findByCode(code);
        if(category.isPresent())
            return category.get();
        return null;
    }


    @Override
    public void delete(String uuid) {
        categoryRepository.deleteById(uuid);
    }
}
