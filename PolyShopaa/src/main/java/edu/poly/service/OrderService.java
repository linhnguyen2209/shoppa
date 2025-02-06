package edu.poly.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import edu.poly.domain.Order;
import edu.poly.domain.OrderDetail;

public interface OrderService {
    Page<Order> listOrders(String keyword, Pageable pageable);

    Optional<Order> findById(long id);

    Order getOrder(long orderId);

    List<OrderDetail> listOrdersDetailByOrderId(long orderId);

    void updateOrderStatus(long orderId, int status);

    Page<Order> findAll(Pageable pageable);

    Order save(Order order);
}
