package com.elcom.id.service;

import com.elcom.id.messaging.rabbitmq.model.User;

public interface UserService {
    void save(User user);

    User findByUsernameAndPassword(String username, String password);
}
