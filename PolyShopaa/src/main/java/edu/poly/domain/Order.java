package edu.poly.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date orderDate;

    @Column(columnDefinition = "nvarchar(255)", nullable = false)
    private String shippingAddress;

    private int status;

    private double total;

    @ManyToOne
    @JoinColumn(name = "username")
    private User user;

    // mappedBy = "order" là mối quan hệ được quản lý bởi thuộc tính order trong
    // OrderDetail.
    // cascade => ử dụng để chỉ định rằng tất cả các hoạt động
    // (hoạt động CRUD: Create, Read, Update, Delete) trên một
    // đối tượng cha sẽ được chuyển tiếp đến các đối tượng con
    // liên quan.
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
    private Set<OrderDetail> orderDetails; // set chỉ lấy các giá trị duy nhất.( không cho phép ptử nào trùng lặp)
}
