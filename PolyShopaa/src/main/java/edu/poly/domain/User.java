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
@Table(name = "Users")
public class User {
    @Id
    @Column(length = 60)
    private String username;

    @Column(columnDefinition = "nvarchar(100)", nullable = false)
    private String password;

    @Column(length = 70, nullable = false, unique = true)
    private String email;

    @Column(length = 10)
    private String role;

    @Temporal(TemporalType.DATE)
    private Date registeredDate;

    private int status;

    @OneToMany(mappedBy = "user")
    private Set<Order> orders;

    @OneToMany(mappedBy = "user")
    private Set<Review> reviews;

    // Thiết lập cascade = CascadeType.ALL nghĩa là khi bạn lưu hoặc cập nhật User,
    // Hibernate sẽ tự động lưu hoặc cập nhật đối tượng UserProfile liên kết với nó.
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserProfile userProfile;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private ShoppingCart shoppingCart;
}
