package com.ambev.core.model;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "items")
@Data
@Builder
@Jacksonized
public class Item {

    @Id
    private String id;

    private Integer quantity;

    private String productId;

    private Double price;
}
