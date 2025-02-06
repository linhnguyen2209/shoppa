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
@Table(name = "Products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long productId;

    @Column(columnDefinition = "nvarchar(100)", nullable = false)
    private String name;

    @Column(columnDefinition = "nvarchar(60)")
    private String image;

    @Column(columnDefinition = "nvarchar(max)")
    private String description;

    @Column(nullable = false)
    private double price;

    private int stock;

    private double discount;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updateAt;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;

    @OneToMany(mappedBy = "product")
    private Set<OrderDetail> orderDetails;
}
