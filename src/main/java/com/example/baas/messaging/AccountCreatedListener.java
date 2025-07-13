package com.example.baas.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class AccountCreatedListener {

    private static final Logger logger = LoggerFactory.getLogger(AccountCreatedListener.class);
    private static final String ACCOUNT_CREATED_TOPIC = "accounts.created";

    @JmsListener(destination = ACCOUNT_CREATED_TOPIC)
    public void handleAccountCreatedEvent(String message) {
        logger.info("Received message from topic '{}': {}", ACCOUNT_CREATED_TOPIC, message);
    }
}