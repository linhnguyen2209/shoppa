package edu.poly.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDto {

	private long orderDetailId; // id không cần validate vì nó tự sinh
	@NotNull(message = "{orderDetail.orderId.notNull}")
	private long orderId;

	@NotNull(message = "{orderDetail.productId.notNull}")
	private long productId;

	@NotNull(message = "{orderDetail.quantity.notNull}")
	@Min(value = 1, message = "{orderDetail.quantity.min}")
	private int quantity;

	@NotNull(message = "{orderDetail.unitPrice.notNull}")
	@Positive(message = "{orderDetail.unitPrice.positive}")
	private double unitPrice;

}
