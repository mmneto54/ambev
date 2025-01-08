package com.ambev.core.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;


@Entity
@Data
@Builder
@NoArgsConstructor  // No-arg constructor for JPA
@AllArgsConstructor // All-arg constructor for @Builder
@Table(name = "orders")
public class Order {

    @Id
    private String id;

    private String customerCpf;

    @Temporal(TemporalType.TIMESTAMP)
    private Date orderDate;

    @OneToMany(mappedBy = "order")
    private List<Item> items;

    private Double totalAmount;

    private String status;

    private String causeStatusDescription;

}
