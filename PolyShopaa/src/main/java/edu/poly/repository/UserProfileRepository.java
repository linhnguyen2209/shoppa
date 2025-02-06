package edu.poly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import edu.poly.domain.UserProfile;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    // Phương thức tìm tất cả các đơn hàng chi tiết bằng orderId
    List<UserProfile> findProfileByUserUsername(String username);
}
