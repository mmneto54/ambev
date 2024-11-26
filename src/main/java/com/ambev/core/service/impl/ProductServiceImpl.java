package com.ambev.core.service.impl;

import com.ambev.core.model.Product;
import com.ambev.core.repository.ProductRepository;
import com.ambev.core.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository repository;

    @Override
    public Page<Product> getAllProducts(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<Product> getProductById(String id) {
        return repository.findById(id);
    }
    //teste usuario github
    @Override
    public Product createProduct(Product product) {
        if (product.getId() == null || product.getId().isEmpty()) {
            product.setId(UUID.randomUUID().toString());
        }
        return repository.save(product);
    }

    @Override
    public Product updateProduct(String id, Product product) {
        return repository.findById(id).map(existingProduct -> {
            existingProduct.setName(product.getName());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setDescription(product.getDescription());
            existingProduct.setQuantityAvailable(product.getQuantityAvailable());
            return repository.save(existingProduct);
        }).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Override
    public void decreaseQuantity(String id, Integer quantity) {
        Product product = this.getProductById(id).orElse(null);
        if(product != null) {
            product.setQuantityAvailable(product.getQuantityAvailable() - quantity);
            updateProduct(product.getId(), product);
        }
    }
}
