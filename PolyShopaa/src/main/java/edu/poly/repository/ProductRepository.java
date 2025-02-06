package edu.poly.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import edu.poly.domain.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByNameContaining(String keyword, Pageable pageable);

    @Query("SELECT MAX(p.id) FROM Product p")
    Long findMaxProductId();
}
