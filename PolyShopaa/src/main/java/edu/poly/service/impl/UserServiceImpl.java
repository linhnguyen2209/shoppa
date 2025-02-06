package edu.poly.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import edu.poly.domain.User;
import edu.poly.domain.UserProfile;
import edu.poly.mapper.UserMapper;
import edu.poly.model.RegisterDto;
import edu.poly.repository.UserProfileRepository;
import edu.poly.repository.UserRepository;
import edu.poly.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserProfileRepository userProfileRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Page<User> search(String keyword, Pageable pageable) {
        return userRepository.findByUsernameContainingOrEmailContaining(keyword, keyword, pageable);
    }

    @Override
    public Optional<User> findById(String username) {
        return userRepository.findById(username);
    }

    @Override
    public UserProfile findProfileByUsername(String username) {
        List<UserProfile> list = userProfileRepository.findProfileByUserUsername(username);
        return list.get(0);
    }

    @Override
    public void save(User User) {
        userRepository.save(User);
    }

    @Override
    public void saveUserProfile(UserProfile UserProfile) {
        userProfileRepository.save(UserProfile);
    }

    @Override
    public void deleteById(String username) {
        userRepository.deleteById(username);
    }

    // đăng ký

    @Autowired
    private UserMapper userMapper;

    // @Autowired
    // private BCryptPasswordEncoder passwordEncoder;

    @Override
    public String saveRegister(RegisterDto registerDto) {
        User user = userMapper.toUser(registerDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        List<User> users = userRepository.findAll();
        for (User u : users) {
            if (u.getUsername().equals(user.getUsername())) {
                return "Tên đăng nhập này đã tồn tại!";
            }
            if (u.getEmail().equals(user.getEmail())) {
                return "Email này đã được sử dụng!";
            }
        }
        userRepository.save(user);
        return "";
    }

    public User authenticate(String username, String password) {
        // Tìm người dùng theo tên đăng nhập
        User user = userRepository.findByUsername(username);
        
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            // Nếu mật khẩu khớp, trả về đối tượng User
            return user;
        } else {
            // Nếu không khớp
            return null;
        }
    }
}
