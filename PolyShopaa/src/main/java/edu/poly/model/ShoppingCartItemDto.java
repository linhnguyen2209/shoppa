package edu.poly.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartItemDto {
    private long cartItemId; // id không cần validate vì nó tự sinh

    @Min(value = 1, message = "{shoppingCartItem.quantity.min}")
    private int quantity;

    @Min(value = 0, message = "{shoppingCartItem.unitPrice.min}")
    private double unitPrice;

    @NotNull(message = "{shoppingCartItem.cartId.notNull}")
    private long cartId;

    @NotNull(message = "{shoppingCartItem.productId.notNull}")
    private long productId;
}
