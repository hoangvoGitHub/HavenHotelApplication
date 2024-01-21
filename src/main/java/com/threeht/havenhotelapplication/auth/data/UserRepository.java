package com.threeht.havenhotelapplication.auth.data;


import com.threeht.havenhotelapplication.auth.data.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);
}
