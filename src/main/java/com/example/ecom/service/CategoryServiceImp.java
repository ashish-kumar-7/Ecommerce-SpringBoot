package com.example.ecom.service;

import com.example.ecom.model.Category;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImp implements CategoryService{

    List<Category> categories = new ArrayList<>();
    private Long id =1L;

    @Override
    public List<Category> getAllCategories() {
        return categories;
    }

    @Override
    public void addCategory(Category category) {
        category.setCategoryID(id++);
        categories.add(category);
    }

    @Override
    public String deleteCategory(Long categoryId) {
        Category category = categories.stream().filter(c -> c.getCategoryID().equals(categoryId)).findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Resource not found"));
        categories.remove(category);
        return "Deleted Successfully";
    }

    @Override
    public Category updateCategory(Category category, Long categoryId) {
        Optional<Category> optionalCategory = categories.stream().filter(c -> c.getCategoryID().equals(categoryId)).findFirst();

        if(optionalCategory.isPresent()){
            Category currentCategory = optionalCategory.get();
            currentCategory.setCategoryName(category.getCategoryName());
            return currentCategory;
        }
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Category not found");
        }
    }
}
