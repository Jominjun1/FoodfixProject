package com.project.foodfix.repository;

import com.project.foodfix.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuItemRepository extends JpaRepository<MenuItem , Long> {
}
