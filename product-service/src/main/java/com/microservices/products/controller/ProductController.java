package com.microservices.products.controller;

import com.microservices.products.domain.Product;
import com.microservices.products.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
public class ProductController {

    @Autowired
    private Environment env;

    @Value("${server.port}")
    private Integer port;

    @Autowired
    private IProductService productService;

    @GetMapping("/")
    public List<Product> getAllProducts() {
        return productService.findAll()
                .stream()
                .map(producto -> {
                    producto.setPort(port);
                    return producto;
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Long id) throws InterruptedException {
        if (id.equals(10L)) {
            throw new IllegalStateException("Product not found!");
        }

        if (id.equals(7L)) {
            TimeUnit.SECONDS.sleep(5L);
        }

        Product product = productService.findById(id);
        product.setPort(Integer.parseInt(env.getProperty("local.server.port")));
        return product;
    }
}
