package com.threeht.havenhotelapplication.service;

import com.threeht.havenhotelapplication.model.User;
import java.util.List;

public interface IUserService {
    List<User> getUsers();
    void deleteUser(String email);
    User getUser(String email);
}
