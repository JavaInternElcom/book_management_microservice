package com.demo.grpc.service;

import com.demo.grpc.User;
import io.grpc.stub.StreamObserver;

public interface UserService {

    void create(User user, StreamObserver<User> userStreamObserver);

}
