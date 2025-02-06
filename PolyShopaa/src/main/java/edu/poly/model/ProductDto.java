package edu.poly.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
// import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private long productId; // id không cần validate vì nó tự sinh

    @NotEmpty(message = "{product.name.notEmpty}")
    private String name;

    // @NotEmpty(message = "{product.image.notEmpty}")
    private String image;

    private MultipartFile imageFile;

    private String description; // Có thể rỗng

    @Positive(message = "{product.price.positive}") // Đảm bảo gtrị số là số dương
    private double price;

    @PositiveOrZero(message = "{product.stock.positiveOrZero}") // giá trị số là số dương hoặc bằng 0
    private int stock;

    @PositiveOrZero(message = "{product.discount.positiveOrZero}")
    private double discount;

    // @PastOrPresent(message = "{product.createAt.pastOrPresent}") // Đảm bảo rằng
    // giá trị ngày không ở tương lai.
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createAt;

    @PastOrPresent(message = "{product.updateAt.pastOrPresent}") // Đảm bảo rằng giá trị ngày không ở tương lai.
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date updateAt;

    @Min(value = 1, message = "{product.categoryId.min}")
    private long categoryId;
}
