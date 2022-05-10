package com.elcom.library.messaging.rabbitmq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQProperties {

    //User service
    @Value("${user.rpc.exchange}")
    public static String USER_RPC_EXCHANGE;

    @Value("${user.rpc.queue}")
    public static String USER_RPC_QUEUE;

    @Value("${user.rpc.key}")
    public static String USER_RPC_KEY;

    @Value("${user.rpc.authen.url}")
    public static String USER_RPC_AUTHEN_URL;

    //library service
    @Value("${library.rpc.exchange}")
    public static String LIBRARY_RPC_EXCHANGE;

    @Value("${library.rpc.queue}")
    public static String LIBRARY_RPC_QUEUE;

    @Value("${library.rpc.key}")
    public static String LIBRARY_RPC_KEY;

    @Autowired
    public RabbitMQProperties(@Value("${user.rpc.exchange}") String userRpcExchange,
                              @Value("${user.rpc.queue}") String userRpcQueue,
                              @Value("${user.rpc.key}") String userRpcKey,
                              @Value("${user.rpc.authen.url}") String userRpcAuthenUrl,
                              @Value("${library.rpc.exchange}") String libraryRpcExchange,
                              @Value("${library.rpc.queue}") String libraryRpcQueue,
                              @Value("${library.rpc.key}") String libraryRpcKey){
        //user
        USER_RPC_EXCHANGE = userRpcExchange;
        USER_RPC_QUEUE = userRpcQueue;
        USER_RPC_KEY = userRpcKey;
        USER_RPC_AUTHEN_URL = userRpcAuthenUrl;

        //library
        LIBRARY_RPC_EXCHANGE = libraryRpcExchange;
        LIBRARY_RPC_QUEUE = libraryRpcQueue;
        LIBRARY_RPC_KEY = libraryRpcKey;
    }
}
