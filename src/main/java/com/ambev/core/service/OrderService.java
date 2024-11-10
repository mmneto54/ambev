package com.ambev.core.service;

import com.ambev.core.model.Order;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface OrderService {

    Page<Order> getAllOrders(Pageable pageable);

    Optional<Order> getOrderById(String id);

    Boolean validateOrder(String order) throws JsonProcessingException;

    void processOrder(String order) throws JsonProcessingException;
}
