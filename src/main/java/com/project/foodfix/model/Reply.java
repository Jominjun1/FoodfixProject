package com.project.foodfix.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "reply")
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long replyId;

    private String adminReply;

    @ManyToOne
    @JoinColumn(name = "review_id", nullable = false, updatable = false)
    private Review review;

}