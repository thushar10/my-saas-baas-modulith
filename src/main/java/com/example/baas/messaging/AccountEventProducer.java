package com.example.baas.messaging;

import com.example.baas.accounts.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class AccountEventProducer {

    private static final Logger logger = LoggerFactory.getLogger(AccountEventProducer.class);
    private static final String ACCOUNT_CREATED_TOPIC = "accounts.created";

    private final JmsTemplate jmsTemplate;

    public AccountEventProducer(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void sendAccountCreatedEvent(Account account) {
        try {
            String message = String.format("Account created: id=%d, name=%s, balance=%.2f",
                    account.getId(), account.getName(), account.getBalance());
            jmsTemplate.convertAndSend(ACCOUNT_CREATED_TOPIC, message);
            logger.info("Sent message to topic '{}': {}", ACCOUNT_CREATED_TOPIC, message);
        } catch (Exception e) {
            logger.error("Error sending message to Solace", e);
        }
    }
}