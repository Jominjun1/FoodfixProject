package com.project.foodfix.repository;

import com.project.foodfix.model.Review;
import com.project.foodfix.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByStore(Store store);
}

