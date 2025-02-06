package edu.poly.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartDto {
    private long cartId; // id không cần validate vì nó tự sinh

    @NotEmpty(message = "{shoppingCart.username.notEmpty}")
    private String username;
}
