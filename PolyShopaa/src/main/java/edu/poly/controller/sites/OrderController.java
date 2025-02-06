package edu.poly.controller.sites;

import edu.poly.domain.Order;
import edu.poly.domain.OrderDetail;
import edu.poly.domain.ShoppingCart;
import edu.poly.domain.ShoppingCartItem;
import edu.poly.domain.User;
import edu.poly.repository.UserRepository;
import edu.poly.service.EmailService;
import edu.poly.service.OrderService;
import edu.poly.service.ShoppingCartService;
import jakarta.servlet.http.HttpSession;
import edu.poly.service.OrderDetailService;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/shopaa/sites/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/checkout")
    public String checkoutOrder(@RequestParam("shippingAddress") String shippingAddress,
            @RequestParam("totalAmount") String totalAmount,
            RedirectAttributes redirectAttributes, HttpSession session) {

        // Lấy thông tin người dùng đang đăng nhập
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        // Loại bỏ ký hiệu $ và thay dấu phẩy thành dấu chấm trước khi chuyển đổi
        String cleanedTotalAmount = totalAmount.replaceAll("[^\\d,]", "").replace(",", ".");
        double totalAmountCast = Double.parseDouble(cleanedTotalAmount);

        // Tạo và lưu đơn hàng mới
        Order order = new Order();
        order.setShippingAddress(shippingAddress);
        order.setOrderDate(new Date());
        order.setTotal(totalAmountCast);
        order.setUser(user);
        order.setStatus(0); // Trạng thái đơn hàng, ví dụ: 0 - Đang chờ

        orderService.save(order);

        // Lấy ra giỏ hàng
        ShoppingCart cart = (ShoppingCart) session.getAttribute("shoppingCart");
        List<ShoppingCartItem> listItems = shoppingCartService.getShoppingCartItems(cart);
        if (listItems != null) {

            // Lưu chi tiết đơn hàng (OrderDetails)
            for (ShoppingCartItem item : listItems) {

                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrder(order);
                orderDetail.setProduct(item.getProduct());
                orderDetail.setQuantity(item.getQuantity());
                orderDetail.setUnitPrice(item.getUnitPrice());

                orderDetailService.save(orderDetail);
            }
        }

        // Gửi email xác nhận đơn hàng
        String subject = "Xác nhận đơn hàng shopaa #" + order.getOrderId();
        // Gọi phương thức gửi email sau khi tạo đơn hàng
        emailService.sendOrderConfirmationEmail(user.getEmail(), subject, order, listItems, shippingAddress,
                totalAmountCast);

        redirectAttributes.addFlashAttribute("message", "Đặt hàng thành công. Hãy kiểm tra thông tin ở email của bạn!");
        shoppingCartService.removeAllItem(); // reset lại giỏ hàng
        return "redirect:/shopaa/sites/cart";
    }

}
