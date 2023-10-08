package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double distance;

    @Column(nullable = false)
    private String status;

}