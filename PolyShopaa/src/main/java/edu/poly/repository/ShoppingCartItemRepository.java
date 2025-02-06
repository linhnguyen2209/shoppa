package edu.poly.repository;

import edu.poly.domain.ShoppingCart;
import edu.poly.domain.ShoppingCartItem;
import edu.poly.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShoppingCartItemRepository extends JpaRepository<ShoppingCartItem, Long> {
    List<ShoppingCartItem> findByCart(ShoppingCart cart);

    Optional<ShoppingCartItem> findByCartAndProduct(ShoppingCart cart, Product product);
}
