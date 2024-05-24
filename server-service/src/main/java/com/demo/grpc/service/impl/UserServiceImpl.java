package com.demo.grpc.service.impl;

import com.demo.grpc.User;
import com.demo.grpc.UserServiceGrpc;
import com.demo.grpc.service.UserService;
import com.demo.grpc.service.model.UserEntity;
import com.demo.grpc.service.repository.UserRepository;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GRpcService
public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void create(User user, StreamObserver<User> userStreamObserver) {
        User userResponse = User.newBuilder()
                .setUsername(user.getUsername())
                .setPassword(user.getPassword())
                .build();
        UserEntity userEntity = UserEntity.fromProto(userResponse);
        userRepository.save(userEntity);
        userStreamObserver.onNext(userResponse);
        userStreamObserver.onCompleted();
    }

}
