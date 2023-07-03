package dev.lycheevm.productrestfulspring.controller;

import dev.lycheevm.productrestfulspring.dto.ProductDto;
import dev.lycheevm.productrestfulspring.model.Product;
import dev.lycheevm.productrestfulspring.repository.ProductRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductRepository repository;

    public ProductController(ProductRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Product> createProduct(@RequestBody @Valid ProductDto productDto) {
        var product = new Product(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(product));
    }
}
