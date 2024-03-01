package com.project.foodfix.controller;

import com.project.foodfix.model.Review;
import com.project.foodfix.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;
    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }
    @PostMapping("/add")
    public ResponseEntity<String> addReview(@RequestBody Review review) {
        reviewService.addReview(review);
        return ResponseEntity.ok("리뷰가 성공적으로 등록되었습니다.");
    }
}
