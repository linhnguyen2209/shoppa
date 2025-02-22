package edu.poly.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.poly.domain.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>{

}
