package com.ambev.core.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor  // No-arg constructor for JPA
@AllArgsConstructor // All-arg constructor for @Builder
@Entity
@Table(name = "products")
public class Product {

    @Id
    private String id;

    private String name;

    private Double price;

    private String description;

    private Integer quantityAvailable;

}
