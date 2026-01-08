package com.coworking.booking.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.jms.ConnectionFactory;
import jakarta.jms.Queue;

@Configuration
public class JmsConfig {

    public static final String CHANGE_LOG_QUEUE = "change-log-queue";

    @Bean
    public ConnectionFactory connectionFactory() {
        return new ActiveMQConnectionFactory("vm://localhost?broker.persistent=false");
    }

    @Bean
    public Queue changeLogQueue() {
        return new ActiveMQQueue(CHANGE_LOG_QUEUE);
    }
}
