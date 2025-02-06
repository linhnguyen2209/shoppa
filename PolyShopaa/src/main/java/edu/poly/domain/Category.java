package edu.poly.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long categoryId;

    @Column(columnDefinition = "nvarchar(100)", nullable = false, unique = true)
    private String categoryName;

    @OneToMany(mappedBy = "category")
    private Set<Product> products;
}
