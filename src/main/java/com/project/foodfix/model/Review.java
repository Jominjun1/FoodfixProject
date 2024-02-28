package com.project.foodfix.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long reviewId;

    private String userNickname;
    private int rating;
    private String photoUrl;
    private String comment;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false, updatable = false)
    private Store store;

}