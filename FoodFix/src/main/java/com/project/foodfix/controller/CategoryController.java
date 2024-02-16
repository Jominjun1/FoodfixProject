package com.project.foodfix.controller;

import com.project.foodfix.model.Category;
import com.project.foodfix.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/{category_name}")
    public Category getCategory(@PathVariable("category_id") Integer category_id) {
        return categoryRepository.findById(category_id).orElse(null);
    }

    @GetMapping("/all")
    public List<Category> getCategoryList() {
        return categoryRepository.findAll();
    }

    @PostMapping("/in/{category_name}")
    public void postCategory(@PathVariable("category_id") Integer category_id, @RequestBody Category category) {
        categoryRepository.save(category);
    }
}
