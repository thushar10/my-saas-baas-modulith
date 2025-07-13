package com.example.baas.config;

import com.solacesystems.jms.SolConnectionFactory;
import com.solacesystems.jms.SolJmsUtility;
import jakarta.jms.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.connection.CachingConnectionFactory; // Good practice for listeners

@Configuration
public class JmsConfig {

    @Value("${solace.jms.host}")
    private String host;

    @Value("${solace.jms.msgVpn}")
    private String msgVpn;

    @Value("${solace.jms.clientUsername}")
    private String username;

    @Value("${solace.jms.clientPassword}")
    private String password;

    // Use CachingConnectionFactory for better performance with listeners
    @Bean
    public CachingConnectionFactory cachingConnectionFactory() throws Exception {
        SolConnectionFactory solConnectionFactory = SolJmsUtility.createConnectionFactory();
        solConnectionFactory.setHost(host);
        solConnectionFactory.setVPN(msgVpn);
        solConnectionFactory.setUsername(username);
        solConnectionFactory.setPassword(password);

        // Optional: Set client ID for durable subscriptions on topics if you had any
        // solConnectionFactory.setClientID("my-app-client-id");

        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(solConnectionFactory);
        cachingConnectionFactory.setSessionCacheSize(10); // Adjust cache size as needed
        return cachingConnectionFactory;
    }

    // JmsTemplate for sending messages (to topics in your case)
    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory cachingConnectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate(cachingConnectionFactory);
        jmsTemplate.setPubSubDomain(true); // Still true, as your producer sends to topics
        return jmsTemplate;
    }

    // JmsListenerContainerFactory for consuming from Queues
    @Bean
    public JmsListenerContainerFactory<?> jmsListenerContainerFactory(
            ConnectionFactory cachingConnectionFactory,
            DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        configurer.configure(factory, cachingConnectionFactory);
        factory.setPubSubDomain(false); // <--- CRUCIAL: Set to false for queues
        factory.setSessionTransacted(false); // Default, but explicit is good
        // factory.setConcurrency("1-1"); // Adjust concurrency as needed for your queue consumers
        return factory;
    }
}