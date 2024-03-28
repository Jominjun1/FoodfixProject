package com.project.foodfix.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "photo")
@Getter
@Setter
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long photo_id;
    private String imagePath;
    @Column(columnDefinition = "VARCHAR(1) DEFAULT '0'")
    private String Photo_status; // 값이 1이면 삭제 대기 상태
}
