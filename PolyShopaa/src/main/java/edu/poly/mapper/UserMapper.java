package edu.poly.mapper;

import edu.poly.domain.User;
import edu.poly.domain.UserProfile;
import edu.poly.model.RegisterDto;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class UserMapper {
    public User toUser(RegisterDto registerDto) {
        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setPassword(registerDto.getPassword());
        user.setEmail(registerDto.getEmail());
        user.setRole("ROLE_CUSTOMER");
        user.setRegisteredDate(new Date());
        user.setStatus(1);

        UserProfile userProfile = new UserProfile();
        userProfile.setFullName(registerDto.getFullName());
        userProfile.setUser(user);

        user.setUserProfile(userProfile);
        return user;
    }
}
