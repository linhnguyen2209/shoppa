package edu.poly.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ShoppingCart")
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cartId;

    @OneToOne
    @JoinColumn(name = "username")
    private User user;

    @OneToMany(mappedBy = "cart")
    private Set<ShoppingCartItem> shoppingCartItems;
}
