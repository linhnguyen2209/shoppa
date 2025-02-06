package edu.poly.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
	private long categoryId; // id không cần validate vì nó tự sinh

	@NotEmpty(message = "{category.name.notEmpty}")
	@Size(min = 5, message = "{category.name.size}")
	private String categoryName;
}
