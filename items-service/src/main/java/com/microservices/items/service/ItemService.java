package com.microservices.items.service;

import com.microservices.items.domain.Item;

import java.util.List;

public interface ItemService {

    List<Item> findAll();

    Item findById(Long id, Integer cantidad);
}
