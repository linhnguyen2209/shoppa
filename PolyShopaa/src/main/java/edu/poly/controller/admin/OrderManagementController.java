package edu.poly.controller.admin;

import edu.poly.domain.Order;
import edu.poly.domain.OrderDetail;
import edu.poly.model.OrderDto;
import edu.poly.service.OrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/shopaa/admin/orders")
public class OrderManagementController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String keyword,
            Model model) {

        if (page < 0) {
            page = 0;
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orderPage = orderService.listOrders(keyword, pageable);

        model.addAttribute("orders", orderPage.getContent());
        model.addAttribute("orderPage", orderPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("orderDto", new OrderDto());
        model.addAttribute("isEditing", false);
        // list orderdetail
        List<OrderDetail> list = new ArrayList<>();
        model.addAttribute("orderDetails", list);

        return "admin/orders/orderManagement";
    }

    @GetMapping("/search")
    public String search(@RequestParam("keyword") String keyword, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("keyword", keyword);
        return "redirect:/shopaa/admin/orders";
    }

    @PostMapping("/update")
    public String updateStatus(@RequestParam("orderId") Long orderId,
            @RequestParam("status") int status,
            RedirectAttributes redirectAttributes) {
        Optional<Order> orderOpt = orderService.findById(orderId);
        if (orderOpt.isPresent()) {
            orderService.updateOrderStatus(orderId, status);
            redirectAttributes.addFlashAttribute("message", "Cập nhật trạng thái đơn hàng thành công!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Đơn hàng không tồn tại!");
        }
        return "redirect:/shopaa/admin/orders";
    }

    @GetMapping("/edit/{id}")
    public ModelAndView editForm(@PathVariable Long id, RedirectAttributes redirectAttributes, ModelMap model) {
        Optional<Order> orderOpt = orderService.findById(id);

        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            OrderDto orderDto = new OrderDto();
            orderDto.setPhoneNumber(order.getUser().getUserProfile().getPhoneNumber());
            orderDto.setUsername(order.getUser().getUsername());
            BeanUtils.copyProperties(order, orderDto);
            model.addAttribute("orderDto", orderDto);
            model.addAttribute("isEditing", true);

            Pageable pageable = PageRequest.of(0, 10);
            Page<Order> orderPage = orderService.findAll(pageable);
            model.addAttribute("orderPage", orderPage);
            model.addAttribute("orders", orderPage.getContent());

            // list orderdetail
            List<OrderDetail> orderDetails = orderService.listOrdersDetailByOrderId(id);
            model.addAttribute("orderDetails", orderDetails);

            return new ModelAndView("admin/orders/orderManagement", model);
        }

        redirectAttributes.addFlashAttribute("error", "Không tìm thấy đơn hàng này!");
        return new ModelAndView("redirect:/shopaa/admin/orders");
    }

    // @GetMapping("/reset")
    // public String reset() {
    // return "redirect:/shopaa/admin/order";
    // }
}
