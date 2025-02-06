package edu.poly.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {
    private long reviewId; // id không cần validate vì nó tự sinh

    @Min(value = 1, message = "{review.rating.min}")
    @Max(value = 5, message = "{review.rating.max}")
    private int rating;

    @NotEmpty(message = "{review.comment.notEmpty}")
    private String comment;

    @NotNull(message = "{review.createdAt.notNull}")
    @PastOrPresent(message = "{review.createdAt.pastOrPresent}")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;

    @Min(value = 1, message = "{review.productId.min}")
    private long productId;

    @NotEmpty(message = "{review.username.notEmpty}")
    private String username;
}
