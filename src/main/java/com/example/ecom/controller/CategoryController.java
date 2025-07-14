package com.example.ecom.controller;

import com.example.ecom.model.Category;
import com.example.ecom.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping("/public/categories")
    public ResponseEntity<List<Category>> getAllCategory(){
        List<Category> categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @PostMapping("/admin/categories")
    public ResponseEntity<String> addCategory(@RequestBody Category category){
        categoryService.addCategory(category);
        return new ResponseEntity<>("Category Added", HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/categories/{categoryID}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryID){
        try{
            String status = categoryService.deleteCategory(categoryID);
            return new ResponseEntity<>(status, HttpStatus.OK);
        } catch (ResponseStatusException e){
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }

    @PutMapping("/admin/categories/{categoryID}")
    public ResponseEntity<String> updateCategory(@RequestBody Category category, @PathVariable Long categoryID){
        try{
            Category updatedCategory = categoryService.updateCategory(category, categoryID);
            return new ResponseEntity<>("Updated", HttpStatus.OK);
        } catch (ResponseStatusException e){
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }

}
