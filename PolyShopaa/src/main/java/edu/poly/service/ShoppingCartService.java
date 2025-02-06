package edu.poly.service;

import edu.poly.domain.Product;
import edu.poly.domain.ShoppingCart;
import edu.poly.domain.ShoppingCartItem;
import edu.poly.domain.User;
import edu.poly.repository.ShoppingCartItemRepository;
import edu.poly.repository.ShoppingCartRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartService {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private ShoppingCartItemRepository shoppingCartItemRepository;

    public ShoppingCart getCurrentCar(User user) {
        // Logic to get current user's cart
        return shoppingCartRepository.findByUser(user);
    }

    public void addProductToCart(Product product, int quantity, ShoppingCart cart) {
        // tìm trong giỏ hàng xem liệu sản phẩm (Product) đã tồn tại trong giỏ hàng chưa
        ShoppingCartItem cartItem = getShoppingCartItems(cart).stream()
                .filter(item -> item.getProduct().getProductId() == product.getProductId())
                .findFirst()
                .orElse(new ShoppingCartItem());

        cartItem.setProduct(product);
        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        cartItem.setUnitPrice(
                cartItem.getProduct().getPrice() * (1 - cartItem.getProduct().getDiscount()) * cartItem.getQuantity());
        cartItem.setCart(cart);

        shoppingCartItemRepository.save(cartItem);
    }

    public void updateCartItem(Long cartItemId, int quantity) {
        ShoppingCartItem cartItem = shoppingCartItemRepository.findById(cartItemId).orElseThrow();
        cartItem.setQuantity(quantity);
        cartItem.setUnitPrice(cartItem.getProduct().getPrice() * (1 - cartItem.getProduct().getDiscount()) * quantity);
        shoppingCartItemRepository.save(cartItem);
    }

    public void removeCartItem(Long cartItemId) {
        shoppingCartItemRepository.deleteById(cartItemId);
    }

    public void removeAllItem() {
        shoppingCartItemRepository.deleteAll();
    }

    // New method to get shopping cart items
    public List<ShoppingCartItem> getShoppingCartItems(ShoppingCart cart) {
        return shoppingCartItemRepository.findByCart(cart);
    }
}
