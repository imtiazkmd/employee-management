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

