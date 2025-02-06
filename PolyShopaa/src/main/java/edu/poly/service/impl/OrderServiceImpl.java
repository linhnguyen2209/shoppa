package edu.poly.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.poly.domain.Order;
import edu.poly.domain.OrderDetail;
import edu.poly.repository.OrderDetailRepository;
import edu.poly.repository.OrderRepository;
import edu.poly.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Override
    public Page<Order> listOrders(String keyword, Pageable pageable) {
        if (keyword == null || keyword.isEmpty()) {
            return orderRepository.findAll(pageable);
        } else {
            return orderRepository.search(keyword, pageable);
        }
    }

    @Override
    public Order getOrder(long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    @Override
    @Transactional
    public void updateOrderStatus(long orderId, int status) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            order.setStatus(status);
            orderRepository.save(order);
        }
    }

    @Override
    public List<OrderDetail> listOrdersDetailByOrderId(long orderId) {
        return orderDetailRepository.findAllByOrderId(orderId);
    }

    @Override
    public Optional<Order> findById(long id) {
        return orderRepository.findById(id);
    }

    @Override
    public Page<Order> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable);
    };

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }
}
