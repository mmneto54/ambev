package com.ambev.core.service.impl;

import com.ambev.util.Utils;
import com.ambev.constant.ConstantsMessages;
import com.ambev.core.enums.OrderStatus;
import com.ambev.core.model.Item;
import com.ambev.core.model.Order;
import com.ambev.core.model.Product;
import com.ambev.core.repository.ItemRepository;
import com.ambev.core.repository.OrderRepository;
import com.ambev.core.service.OrderService;
import com.ambev.core.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.ambev.constant.ConstantsMessages.INVALID_CPF;
import static com.ambev.constant.ConstantsMessages.UNFOUND_PRODUCT;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ProductService productService;

    @Override
    public Page<Order> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    @Override
    public Optional<Order> getOrderById(String id) {
        return orderRepository.findById(id);
    }

    @Override
    public Boolean validateOrder(String message) throws JsonProcessingException {

        Order orderRequest = new ObjectMapper().readValue(message, Order.class);

        if (!Utils.isValidCPF(orderRequest.getCustomerCpf())) {
            log.error("Invalid CPF on order: {} the order will be rejected", orderRequest.getId());
            Order order = buildOrder(orderRequest, OrderStatus.REJECTED, INVALID_CPF, new ArrayList<>());
            orderRepository.save(order);
            return false;
        }

        // - Verificar se o pedido e os pedidos na lista existem
        boolean inconsistentProducts = false;
        List<Product> products = new ArrayList<>();
        for(Item i : orderRequest.getItems()) {
            var product = productService.getProductById(i.getProductId()).orElse(null);
            if(product == null || (product.getQuantityAvailable() < i.getQuantity())) {
                inconsistentProducts = true;
                break;
            }
            i.setPrice(product.getPrice());
            products.add(product);
        }

        if(inconsistentProducts) {
            log.error("Invalid products on order: {} the order will be rejected", orderRequest.getId());
            Order order = buildOrder(orderRequest, OrderStatus.REJECTED, UNFOUND_PRODUCT, new ArrayList<>());
            orderRepository.save(order);
            return false;
        }

        orderRequest.getItems().forEach(p -> {
            productService.decreaseQuantity(p.getProductId(), p.getQuantity());
        });

        return true;
    }

    @Override
    @Transient
    public void processOrder(String message) throws JsonProcessingException {
        Order orderJson = new ObjectMapper().readValue(message, Order.class);
        Order orderEntity = orderRepository.findById(orderJson.getId()).orElse(orderJson);

        orderEntity.setStatus(OrderStatus.COMPLETED.name());
        orderEntity.setCauseStatusDescription(ConstantsMessages.SUCCESSFUL_ORDER);
        itemRepository.saveAll(orderEntity.getItems());
        orderRepository.save(orderEntity);
    }

    private Order buildOrder(Order order, OrderStatus orderStatus, String causeStatusDescription, List<Product> products) {
        return Order.builder()
                .orderDate(new Date())
                .customerCpf(order.getCustomerCpf())
                .id(order.getId())
                .totalAmount(getTotalAmount(products, order.getItems()))
                .status(orderStatus.name())
                .causeStatusDescription(causeStatusDescription)
                .items(order.getItems())
                .build();
    }

    private Double getTotalAmount(List<Product> products, List<Item> items) {
        return !shouldCalculateAmount(products, items)
                ? 0.0
                : items.stream()
                .mapToDouble(item -> {
                    Product product = products.stream()
                            .filter(p -> p.getId().equals(item.getProductId()))
                            .findFirst()
                            .orElse(Product.builder().price(0.0).build());
                    return product.getPrice() * item.getQuantity();
                }).sum();
    }

    private boolean shouldCalculateAmount(List<Product> products, List<Item> items){
        return products == null || items == null || items.isEmpty() || products.isEmpty();
    }

}
