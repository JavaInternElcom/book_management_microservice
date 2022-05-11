package com.elcom.bookloan.messaging.rabbitmq;

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

    //book-loan service
    @Value("${book-loan.rpc.exchange}")
    public static String BOOK_LOAN_RPC_EXCHANGE;

    @Value("${book-loan.rpc.queue}")
    public static String BOOK_LOAN_RPC_QUEUE;

    @Value("${book-loan.rpc.key}")
    public static String BOOK_LOAN_RPC_KEY;

    @Autowired
    public RabbitMQProperties(@Value("${user.rpc.exchange}") String userRpcExchange,
                              @Value("${user.rpc.queue}") String userRpcQueue,
                              @Value("${user.rpc.key}") String userRpcKey,
                              @Value("${user.rpc.authen.url}") String userRpcAuthenUrl,
                              @Value("${book-loan.rpc.exchange}") String bookLoanRpcExchange,
                              @Value("${book-loan.rpc.queue}") String bookLoanRpcQueue,
                              @Value("${book-loan.rpc.key}") String bookLoanRpcKey){
        //user
        USER_RPC_EXCHANGE = userRpcExchange;
        USER_RPC_QUEUE = userRpcQueue;
        USER_RPC_KEY = userRpcKey;
        USER_RPC_AUTHEN_URL = userRpcAuthenUrl;

        //book-loan
        BOOK_LOAN_RPC_EXCHANGE = bookLoanRpcExchange;
        BOOK_LOAN_RPC_QUEUE = bookLoanRpcQueue;
        BOOK_LOAN_RPC_KEY = bookLoanRpcKey;
    }
}
