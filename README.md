# Employee Management System

## Overview
Spring Boot application for managing employees, departments, leave requests, and notifications using RabbitMQ.

## Tech Stack
- Java 17
- Spring Boot
- Spring Security (Basic Auth)
- MySQL
- RabbitMQ
- Docker & Docker Compose

## Setup Instructions

### Prerequisites
- Java 17
- Maven 3.9+
- MySQL 8+
- Docker (optional)

### Run locally
mvn clean install
mvn spring-boot:run "-Dspring-boot.run.profiles=local"

### Run with Docker
docker-compose up --build

## PostMan Collection
-https://lunar-robot-71097.postman.co/workspace/My-Workspace~4c2f95c6-2177-472d-8f32-67228e6adbbb/collection/20256648-30524ac2-fe56-485c-8faa-6bb280923af1?action=share&creator=20256648
-https://lunar-robot-71097.postman.co/workspace/My-Workspace~4c2f95c6-2177-472d-8f32-67228e6adbbb/collection/20256648-704bb102-69e6-48a5-9e39-751cf75056b7?action=share&creator=20256648

## Architecture Overview
- RESTful layered architecture
- Service-level authorization
- Asynchronous notifications via RabbitMQ
- Containerized infrastructure

## Security
- Basic Authentication
- Role-based authorization (ADMIN, USER)
- Ownership-based data access for USER role

## Notification System
- Employee creation → welcome notification
- Leave status update → status notification
- Asynchronous processing using RabbitMQ
- Secure message deserialization

## API Documentation
(mention Bruno / Postman collection)

## Setup Instructions
### Using Docker
```bash
docker compose up --build

