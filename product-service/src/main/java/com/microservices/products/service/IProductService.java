package com.microservices.products.service;

import com.microservices.products.domain.Product;

import java.util.List;

public interface IProductService {
    List<Product> findAll();

    Product findById(Long id);
}
