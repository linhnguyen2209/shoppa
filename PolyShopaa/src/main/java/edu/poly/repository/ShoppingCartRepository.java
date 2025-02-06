package edu.poly.repository;

import edu.poly.domain.ShoppingCart;
import edu.poly.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    ShoppingCart findByUser(User user);
}
