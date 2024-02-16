package com.project.foodfix.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
public class Category {
    @Id
    private Integer category_id;
    private String category_name;

    public Category(){

    }
}
