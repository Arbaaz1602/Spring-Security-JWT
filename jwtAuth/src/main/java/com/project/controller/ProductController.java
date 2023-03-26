package com.project.controller;

import java.net.URI;
import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.entity.Product;
import com.project.repository.ProductRepository;

@RestController
@RequestMapping("/products")
public class ProductController {
	
	@Autowired
	private ProductRepository productRepository;
	
	@PostMapping
	public ResponseEntity<Product> create (@RequestBody @jakarta.validation.Valid Product product)
	{
		Product savedProduct = productRepository.save(product);
		URI productUri = URI.create("/products/" + savedProduct.getId());
		return ResponseEntity.created(productUri).body(savedProduct);
	}
	
	@GetMapping
	private List<Product> list()
	{
		return productRepository.findAll();
	}

}
