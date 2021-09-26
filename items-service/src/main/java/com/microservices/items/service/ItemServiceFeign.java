package com.microservices.items.service;

import com.microservices.items.client.ProductClientRest;
import com.microservices.items.domain.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("serviceFeign")
public class ItemServiceFeign implements ItemService {

    @Autowired
    private ProductClientRest clientRest;

    @Override
    public List<Item> findAll() {
        return clientRest.getAllProducts()
                .stream()
                .map(product -> new Item(product, 1))
                .collect(Collectors.toList());
    }

    @Override
    public Item findById(Long id, Integer cantidad) {
        return new Item(clientRest.getProduct(id), cantidad);
    }

}
