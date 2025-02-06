package edu.poly.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import edu.poly.domain.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE " +
            "o.shippingAddress LIKE %?1% OR " +
            "o.user.username LIKE %?1% OR " +
            "CAST(o.orderId AS string) LIKE %?1%")
    Page<Order> search(String keyword, Pageable pageable);
}
