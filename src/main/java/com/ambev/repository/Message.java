package com.ambev.repository;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Table(name = "messages")
public class Message {

    @Id
    private String id;
    private String content;

}