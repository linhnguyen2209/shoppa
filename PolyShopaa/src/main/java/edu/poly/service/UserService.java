package edu.poly.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import edu.poly.domain.User;
import edu.poly.domain.UserProfile;
import edu.poly.model.RegisterDto;

public interface UserService {

    Page<User> findAll(Pageable pageable);

    Page<User> search(String keyword, Pageable pageable);

    Optional<User> findById(String username);

    void save(User User);

    void saveUserProfile(UserProfile UserProfile);

    UserProfile findProfileByUsername(String username);

    void deleteById(String username);

    String saveRegister(RegisterDto registerDto);

    User authenticate(String username, String password);
}
