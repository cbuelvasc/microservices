package com.microservices.items.controller;

import com.microservices.items.domain.Item;
import com.microservices.items.domain.Product;
import com.microservices.items.service.ItemService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
public class ItemController {

    private final Logger logger = LoggerFactory.getLogger(ItemController.class);

    @Autowired
    private CircuitBreakerFactory circuitBreakerFactory;

    @Autowired
    @Qualifier("serviceFeign")
    private ItemService itemService;

    @GetMapping("/")
    public List<Item> getAllItems() {
        return itemService.findAll();
    }

    @GetMapping("/{id}/quantity/{quantity}")
    public Item getDetail(@PathVariable Long id, @PathVariable Integer quantity) {
        return circuitBreakerFactory.create("items")
                .run(() -> itemService.findById(id, quantity), e -> getDetailCircuitBreak(id, quantity, e));
    }

    @GetMapping("/v2/{id}/quantity/{quantity}")
    @CircuitBreaker(name = "items", fallbackMethod = "getDetailCircuitBreak")
    public Item getDetailCircuitBreakAnnotations(@PathVariable Long id, @PathVariable Integer quantity) {
        return itemService.findById(id, quantity);
    }

    @GetMapping("/v3/{id}/quantity/{quantity}")
    @CircuitBreaker(name = "items", fallbackMethod = "getDetailCircuitBreakTimeLimiter")
    @TimeLimiter(name = "items")
    public CompletableFuture<Item> getDetailCircuitBreakTimeLimiter(@PathVariable Long id, @PathVariable Integer quantity) {
        return CompletableFuture.supplyAsync(() -> itemService.findById(id, quantity));
    }

    public Item getDetailCircuitBreak(Long id, Integer quantity, Throwable e) {
        logger.info(e.getMessage());
        Product producto = new Product();
        producto.setId(id);
        producto.setName("Camara Sony");
        producto.setPrice(500.00);
        return new Item(producto, quantity);
    }

    public CompletableFuture<Item> getDetailCircuitBreakTimeLimiter(Long id, Integer quantity, Throwable e) {
        logger.info(e.getMessage());
        Product producto = new Product();
        producto.setId(id);
        producto.setName("Camara Sony");
        producto.setPrice(500.00);
        return CompletableFuture.supplyAsync(() -> new Item(producto, quantity));
    }
}
