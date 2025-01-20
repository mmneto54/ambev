package com.ambev.core.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor  // No-arg constructor for JPA
@AllArgsConstructor // All-arg constructor for @Builder
@Entity
@Table(name = "items")
public class Item {

    @Id
    private String id;

    private Integer quantity;

    private String productId;

    private Double price;

    @ManyToOne // This is the "many" side of the relationship
    private Order order;

}
