package edu.poly.service.impl;

import edu.poly.domain.Product;
import edu.poly.repository.ProductRepository;
import edu.poly.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Page<Product> search(String keyword, Pageable pageable) {
        return productRepository.findByNameContaining(keyword, pageable);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public void save(Product product) {
        productRepository.save(product);
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Long getMaxProductId() {
        return productRepository.findMaxProductId();
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}
