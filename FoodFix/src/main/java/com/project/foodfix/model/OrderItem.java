package com.project.foodfix.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "order_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long orderItemId;

    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false, updatable = false)
    private Menu menu;

    private Integer  quantity;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "takeout_id", nullable = false, updatable = false)
    private TakeoutOrder takeoutOrder;
}
