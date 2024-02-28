package com.project.foodfix.repository;

import com.project.foodfix.model.Reply;
import com.project.foodfix.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findByReview(Review review);
}