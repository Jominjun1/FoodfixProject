package com.project.foodfix.service;

import com.project.foodfix.model.Review;
import com.project.foodfix.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }
    public void addReview(Review review) {
        reviewRepository.save(review);
    }
}