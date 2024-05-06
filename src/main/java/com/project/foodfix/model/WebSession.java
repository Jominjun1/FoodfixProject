package com.project.foodfix.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "webSession")
public class WebSession {
    @Id
    @Column(name = "session_id", unique = true)
    private String session_id;

    private String user_id;
    private Long store_id;
}
