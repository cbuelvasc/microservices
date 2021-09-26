package com.microservices.items.client;

import com.microservices.items.domain.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "products-service")
public interface ProductClientRest {
	
	@GetMapping("/")
	List<Product> getAllProducts();
	
	@GetMapping("/{id}")
	Product getProduct(@PathVariable Long id);
}
