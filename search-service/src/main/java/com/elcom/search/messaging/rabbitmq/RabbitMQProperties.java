package com.elcom.search.messaging.rabbitmq;

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

    //search service
    @Value("${search.rpc.exchange}")
    public static String SEARCH_RPC_EXCHANGE;

    @Value("${search.rpc.queue}")
    public static String SEARCH_RPC_QUEUE;

    @Value("${search.rpc.key}")
    public static String SEARCH_RPC_KEY;

    @Autowired
    public RabbitMQProperties(@Value("${user.rpc.exchange}") String userRpcExchange,
                              @Value("${user.rpc.queue}") String userRpcQueue,
                              @Value("${user.rpc.key}") String userRpcKey,
                              @Value("${user.rpc.authen.url}") String userRpcAuthenUrl,
                              @Value("${search.rpc.exchange}") String searchRpcExchange,
                              @Value("${search.rpc.queue}") String searchRpcQueue,
                              @Value("${search.rpc.key}") String searchRpcKey){
        //user
        USER_RPC_EXCHANGE = userRpcExchange;
        USER_RPC_QUEUE = userRpcQueue;
        USER_RPC_KEY = userRpcKey;
        USER_RPC_AUTHEN_URL = userRpcAuthenUrl;

        //search
        SEARCH_RPC_EXCHANGE = searchRpcExchange;
        SEARCH_RPC_QUEUE = searchRpcQueue;
        SEARCH_RPC_KEY = searchRpcKey;
    }
}
