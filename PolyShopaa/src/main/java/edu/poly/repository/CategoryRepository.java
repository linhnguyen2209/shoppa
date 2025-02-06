package edu.poly.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.poly.domain.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{
	Page<Category> findByCategoryNameContaining(String keyword, Pageable pageable);
}
