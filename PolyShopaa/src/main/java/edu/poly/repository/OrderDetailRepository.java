package edu.poly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import edu.poly.domain.OrderDetail;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    // Phương thức tìm tất cả các đơn hàng chi tiết bằng orderId
    @Query("SELECT od FROM OrderDetail od WHERE od.order.orderId = :orderId")
    List<OrderDetail> findAllByOrderId(Long orderId);
}
