package dev.lycheevm.productrestfulspring.controller;

import dev.lycheevm.productrestfulspring.dto.ProductDto;
import dev.lycheevm.productrestfulspring.model.Product;
import dev.lycheevm.productrestfulspring.repository.ProductRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> productList = repository.findAll();

        productList.forEach(product -> {
            var id = product.getId();
            product.add(linkTo(methodOn(ProductController.class).getProduct(id)).withSelfRel());
        });

        return ResponseEntity.status(HttpStatus.OK).body(productList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getProduct(@PathVariable(value = "id") UUID id) {
        Optional<Product> product = repository.findById(id);
        if(product.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
        product.get().add(linkTo(methodOn(ProductController.class).getAllProducts()).withRel("Product list"));
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable(value = "id") UUID id, @RequestBody @Valid ProductDto productDto) {
        var productSearched = repository.findById(id);
        if (productSearched.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
        var product = productSearched.get();
        BeanUtils.copyProperties(productDto, product);
        return ResponseEntity.status(HttpStatus.OK).body(repository.save(product));
    }

    public ResponseEntity<Object> deleteProduct(@PathVariable(value = "id") UUID id) {
        var productSearched = repository.findById(id);
        if (productSearched.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
        repository.delete(productSearched.get());
        return ResponseEntity.status(HttpStatus.OK).body("Product deleted successfully");

    }
}
