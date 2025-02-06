package edu.poly.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.poly.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Page<User> findByUsernameContainingOrEmailContaining(String username, String email, Pageable pageable);

    User findByUsernameAndPassword(String username, String password);

    User findByUsername(String username);
}
