package com.ambev.core.model;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "orders")
@Data
@Builder
@Jacksonized
public class Order {

    @Id
    private String id;

    private String customerCpf;

    private Date orderDate;

    @DBRef
    private List<Item> items;

    private Double totalAmount;

    private String status;

    private String causeStatusDescription;
}
