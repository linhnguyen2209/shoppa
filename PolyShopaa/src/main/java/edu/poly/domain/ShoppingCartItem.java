package edu.poly.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ShoppingCartItems")
public class ShoppingCartItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long cartItemId;
	@Column(nullable = false)
	private int quantity;

	private double unitPrice;

	@ManyToOne
	@JoinColumn(name = "cartId")
	private ShoppingCart cart;

	@ManyToOne
	@JoinColumn(name = "productId")
	private Product product;
}
