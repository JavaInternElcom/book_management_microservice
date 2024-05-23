package com.elcom.id.service;

import com.elcom.id.model.User;

public interface UserService {
    void save(User user);

    User findByUsernameAndPassword(String username, String password);
}
