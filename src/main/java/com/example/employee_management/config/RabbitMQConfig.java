package com.example.employee_management.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class RabbitMQConfig {

    public static final String EMPLOYEE_QUEUE = "employee.notification.queue";
    public static final String LEAVE_QUEUE = "leave.notification.queue";

    @Bean
    public Queue employeeQueue() {
        return new Queue(EMPLOYEE_QUEUE, true);
    }

    @Bean
    public Queue leaveQueue() {
        return new Queue(LEAVE_QUEUE, true);
    }

    // ✅ Secure deserialization (whitelist your DTOs)
    @Bean
    public SimpleMessageConverter messageConverter() {
        SimpleMessageConverter converter = new SimpleMessageConverter();
        converter.setAllowedListPatterns(
                Collections.singletonList("com.example.employee_management.dto.*")
        );
        return converter;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(
            ConnectionFactory connectionFactory,
            SimpleMessageConverter messageConverter
    ) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }

}

