package edu.poly.controller.sites;

import edu.poly.domain.Product;
import edu.poly.domain.ShoppingCart;
import edu.poly.domain.ShoppingCartItem;
import edu.poly.service.ProductService;
import edu.poly.service.ShoppingCartService;
import jakarta.servlet.http.HttpSession;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("shopaa/sites")
public class ShoppingCartPageController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private ProductService productService;

    @Autowired
    HttpSession session;

    @GetMapping("/cart")
    public String showCart(Model model, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("loggedInUser") != null) {
            ShoppingCart cart = (ShoppingCart) session.getAttribute("shoppingCart");
            model.addAttribute("cart", cart);
            List<ShoppingCartItem> shoppingCartItems = shoppingCartService.getShoppingCartItems(cart);
            model.addAttribute("shoppingCartItems", shoppingCartItems);
            model.addAttribute("totalPriceInCart", calculateTotalPrice(shoppingCartItems));
            return "sites/products/shopping-cart";
        } else {
            return "redirect:/shopaa/sites/login";
        }

    }

    @PostMapping("/cart/add")
    public String addProductToCart(@RequestParam("productId") Long productId,
            @RequestParam("quantity") int quantity) {

        Optional<Product> opt = productService.findById(productId);
        if (opt.isPresent()) {
            Product product = opt.get();
            ShoppingCart cart = (ShoppingCart) session.getAttribute("shoppingCart");
            shoppingCartService.addProductToCart(product, quantity, cart);
            getTotalCartItem(cart, session); // cập nhật lại sl sp sau khi thêm
        }
        return "redirect:/shopaa/sites/cart";
    }

    @PostMapping("/cart/update")
    public String updateCartItem(@RequestParam("cartItemId") Long cartItemId,
            @RequestParam("quantity") int quantity) {
        shoppingCartService.updateCartItem(cartItemId, quantity);
        return "redirect:/shopaa/sites/cart";
    }

    @PostMapping("/cart/remove")
    public String removeCartItem(@RequestParam("cartItemId") Long cartItemId) {
        shoppingCartService.removeCartItem(cartItemId);
        // cập nhật lại tổng số lượng trong giỏ
        ShoppingCart cart = (ShoppingCart) session.getAttribute("shoppingCart");
        getTotalCartItem(cart, session); // cập nhật lại sl sp sau khi xóa
        return "redirect:/shopaa/sites/cart";
    }

    public String calculateTotalPrice(List<ShoppingCartItem> shoppingCartItems) {
        double totalPriceInCart = 0;
        if (!shoppingCartItems.isEmpty()) {
            for (ShoppingCartItem shoppingCartItem : shoppingCartItems) {
                // Làm tròn giá trị đơn lẻ trước khi cộng dồn
                double unitPrice = shoppingCartItem.getUnitPrice();
                totalPriceInCart += unitPrice;
            }
        }

        // Làm tròn tổng giá sau khi cộng dồn
        DecimalFormat df = new DecimalFormat("$#,##0.00");
        String formattedTotalPrice = df.format(totalPriceInCart);

        return formattedTotalPrice;
    }

    public void getTotalCartItem(ShoppingCart shoppingCart, HttpSession session) {
        // lấy danh sách sản phẩm trong giỏ
        int totalQuantity = 0;
        List<ShoppingCartItem> shoppingCartItems = shoppingCartService
                .getShoppingCartItems(shoppingCart);
        if (!shoppingCartItems.isEmpty()) {
            totalQuantity = shoppingCartItems.size();
        }
        session.setAttribute("totalCartItem", totalQuantity);
    }
}
