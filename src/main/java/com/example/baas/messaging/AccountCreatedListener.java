package com.example.baas.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class AccountCreatedListener {

    private static final Logger logger = LoggerFactory.getLogger(AccountCreatedListener.class);
    // This should match the QUEUE_NAME in your configure-solace.sh script
    private static final String ACCOUNT_CREATED_QUEUE = "q/accounts/created";

    @JmsListener(destination = ACCOUNT_CREATED_QUEUE, containerFactory = "jmsListenerContainerFactory")
    public void receiveAccountCreatedEvent(String message) {
        logger.info("Received message from queue '{}': {}", ACCOUNT_CREATED_QUEUE, message);
        // Process the account created event here
        // For example, parse the message and update a database, send notifications, etc.
    }
}