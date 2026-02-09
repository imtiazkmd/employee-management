package com.example.employee_management.service;

import com.example.employee_management.config.RabbitMQConfig;
import com.example.employee_management.dto.EmployeeNotificationMessage;
import com.example.employee_management.dto.LeaveNotificationMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationConsumer {

    @RabbitListener(queues = RabbitMQConfig.EMPLOYEE_QUEUE)
    public void handleEmployeeNotification(EmployeeNotificationMessage message) {
        log.info("Received employee notification: {}", message);

        // Simulate email sending
        log.info("Sending welcome email to {}", message.email());
    }

    @RabbitListener(queues = RabbitMQConfig.LEAVE_QUEUE)
    public void handleLeaveNotification(LeaveNotificationMessage message) {
        log.info("Received leave notification: {}", message);

        // Simulate email sending
        log.info(
                "Sending leave status email: Leave {} is {}",
                message.leaveId(),
                message.status()
        );
    }
}

