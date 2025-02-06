package edu.poly.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

    private long orderId; // id không cần validate vì nó tự sinh

    @NotNull(message = "{order.date.notNull}")
    @PastOrPresent(message = "{order.date.pastOrPresent}")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date orderDate;

    @NotEmpty(message = "{order.shippingAddress.notEmpty}")
    private String shippingAddress;

    @NotBlank(message = "{order.phoneNumber.notBlank}")
    @Pattern(regexp = "^(\\+\\d{1,3}[- ]?)?\\d{10}$", message = "{order.phoneNumber.valid}")
    private String phoneNumber;

    @Min(value = 0, message = "{order.status.min}")
    private int status;

    @PositiveOrZero(message = "{order.total.positiveOrZero}")
    private double total;

    @NotEmpty(message = "{order.username.notEmpty}")
    private String username;
}
