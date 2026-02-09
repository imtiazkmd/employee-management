package com.example.employee_management.service;

import com.example.employee_management.config.RabbitMQConfig;
import com.example.employee_management.dto.EmployeeNotificationMessage;
import com.example.employee_management.dto.LeaveNotificationMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void sendEmployeeCreated(EmployeeNotificationMessage message) {
        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EMPLOYEE_QUEUE,
                    message
            );
            log.info("Employee notification sent: {}", message);
        } catch (Exception e) {
            log.error("Failed to send employee notification", e);
        }
    }

    public void sendLeaveStatusUpdate(LeaveNotificationMessage message) {
        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.LEAVE_QUEUE,
                    message
            );
            log.info("Leave notification sent: {}", message);
        } catch (Exception e) {
            log.error("Failed to send leave notification", e);
        }
    }
}

