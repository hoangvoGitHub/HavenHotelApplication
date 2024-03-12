package com.threeht.havenhotelapplication.service;

import com.threeht.havenhotelapplication.model.User;
import java.util.List;

public interface IUserService {
    List<User> getUsers();
    void deleteUser(Long userId);
    User getUserByEmail(String email);
    User getUserById(Long userId);
}
