package edu.poly.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import edu.poly.domain.Product;

public interface ProductService {

    Page<Product> findAll(Pageable pageable);

    Page<Product> search(String keyword, Pageable pageable);

    Optional<Product> findById(Long id);

    void save(Product product);

    void deleteById(Long id);

    Long getMaxProductId();

    List<Product> getAllProducts();
}
