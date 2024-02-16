package com.project.foodfix.repository;


import com.project.foodfix.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category , Integer> {
}
