package com.ambev.repository;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "messages")
@Data
public class Message {

    @Id
    private String id;
    private String content;

}