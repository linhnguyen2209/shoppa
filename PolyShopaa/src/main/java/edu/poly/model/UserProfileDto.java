package edu.poly.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDto {
    private long profileId;

    // @NotBlank(message = "{userProfile.fullName.notBlank}")
    // @Size(min = 3, max = 100, message = "{userProfile.fullName.size}")
    private String fullName;

    private String avatar;

    private MultipartFile avatarFile;

    // @NotBlank(message = "{userProfile.phoneNumber.notBlank}")
    // @Pattern(regexp = "^(\\+\\d{1,3}[- ]?)?\\d{10}$", message = "{userProfile.phoneNumber.valid}")
    private String phoneNumber;

    // @NotNull(message = "{userProfile.dateOfBirth.notNull}")
    // @Past(message = "{userProfile.dateOfBirth.past}")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateOfBirth;

    // @NotBlank(message = "{userProfile.shippingAddress.notBlank}")
    // @Size(min = 5, max = 200, message = "{userProfile.shippingAddress.size}")
    private String shippingAddress;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String updatedAt;

    // @NotBlank(message = "{userProfile.username.notBlank}")
    private String username;
}
