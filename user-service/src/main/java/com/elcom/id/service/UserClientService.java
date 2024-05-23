package com.elcom.id.service;

import com.demo.grpc.User;
import com.demo.grpc.UserServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserClientService {

    Logger LOGGER = LoggerFactory.getLogger(UserClientService.class);

    private ManagedChannel managedChannel;
    private UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub;

    public UserClientService() {
        this.managedChannel = ManagedChannelBuilder.forAddress("localhost", 6565)
                // Channels are secure by default (via SSL/TLS). For the example
                // we disable TLS to avoid
                // needing certificates.
                .usePlaintext().build();
        this.userServiceBlockingStub = UserServiceGrpc.newBlockingStub(managedChannel);
    }

    public User createUser(String username, String password) {
        User user = User.newBuilder().setUsername(username).setPassword(password).build();
        User newUser = userServiceBlockingStub.create(user);
        return newUser;
    }
}
