package com.example.ecom.service;

import com.example.ecom.payload.CategoryDTO;
import com.example.ecom.payload.CategoryResponse;


public interface CategoryService {
    CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    CategoryDTO addCategory(CategoryDTO categoryDTO);
    CategoryDTO deleteCategory(Long categoryID);
    CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryID);
}
