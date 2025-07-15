package com.example.ecom.service;

import com.example.ecom.model.Category;
import com.example.ecom.payload.CategoryDTO;
import com.example.ecom.payload.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    CategoryDTO addCategory(CategoryDTO categoryDTO);
    CategoryDTO deleteCategory(Long categoryID);
    CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryID);
}
