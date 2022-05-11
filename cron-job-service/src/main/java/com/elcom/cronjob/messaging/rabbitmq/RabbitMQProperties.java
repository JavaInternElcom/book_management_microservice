package com.elcom.cronjob.messaging.rabbitmq;

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

    //cron-job service
    @Value("${cron-job.rpc.exchange}")
    public static String CRON_JOB_RPC_EXCHANGE;

    @Value("${cron-job.rpc.queue}")
    public static String CRON_JOB_RPC_QUEUE;

    @Value("${cron-job.rpc.key}")
    public static String CRON_JOB_RPC_KEY;

    @Autowired
    public RabbitMQProperties(@Value("${user.rpc.exchange}") String userRpcExchange,
                              @Value("${user.rpc.queue}") String userRpcQueue,
                              @Value("${user.rpc.key}") String userRpcKey,
                              @Value("${user.rpc.authen.url}") String userRpcAuthenUrl,
                              @Value("${cron-job.rpc.exchange}") String cronJobRpcExchange,
                              @Value("${cron-job.rpc.queue}") String cronJobRpcQueue,
                              @Value("${cron-job.rpc.key}") String cronJobRpcKey){
        //user
        USER_RPC_EXCHANGE = userRpcExchange;
        USER_RPC_QUEUE = userRpcQueue;
        USER_RPC_KEY = userRpcKey;
        USER_RPC_AUTHEN_URL = userRpcAuthenUrl;

        //cron-job
        CRON_JOB_RPC_EXCHANGE = cronJobRpcExchange;
        CRON_JOB_RPC_QUEUE = cronJobRpcQueue;
        CRON_JOB_RPC_KEY = cronJobRpcKey;
    }
}