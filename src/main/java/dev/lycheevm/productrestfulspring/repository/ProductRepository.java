package dev.lycheevm.productrestfulspring.repository;

import dev.lycheevm.productrestfulspring.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
}
