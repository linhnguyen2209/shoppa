package edu.poly.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @NotBlank(message = "{user.username.notBlank}")
    @Size(min = 3, max = 50, message = "{user.username.size}")
    private String username;

    @NotBlank(message = "{user.password.notBlank}")
    @Size(min = 6, message = "{user.password.size}")
    private String password;

    @NotBlank(message = "{user.email.notBlank}")
    @Email(message = "{user.email.valid}")
    private String email;

    @NotBlank(message = "{user.role.notBlank}")
    private String role;

    @NotNull(message = "{user.registeredDate.notNull}")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date registeredDate;

    @NotNull(message = "{user.status.notNull}")
    private int status;
}
