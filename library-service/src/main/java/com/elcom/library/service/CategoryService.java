package com.elcom.library.service;

import com.elcom.library.model.Category;

import java.util.List;

public interface CategoryService {

    List<Category> findAll();

    void save(Category category);

    Category findByUuid(String uuid);

    Category findByName(String name);

    Category findByCode(String code);

    void delete(String uuid);
}
