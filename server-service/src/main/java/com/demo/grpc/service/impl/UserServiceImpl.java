package com.demo.grpc.service.impl;

import com.demo.grpc.User;
import com.demo.grpc.UserServiceGrpc;
import com.demo.grpc.service.UserService;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;

@GRpcService
public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase implements UserService {

    @Override
    public void create(User user, StreamObserver<User> userStreamObserver) {
        User userResponse = User.newBuilder()
                .setId(1)
                .setUsername(user.getUsername())
                .setPassword(user.getPassword())
                .build();
        userStreamObserver.onNext(userResponse);
        userStreamObserver.onCompleted();
    }
}
