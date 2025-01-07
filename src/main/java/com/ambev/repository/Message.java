package com.ambev.repository;


import jakarta.persistence.Table;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Table(name = "messages")
@Data
public class Message {

    @Id
    private String id;
    private String content;

}