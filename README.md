# RestAPI

Simple Spring Boot REST API for managing Tasks.

## Overview
This project exposes CRUD endpoints for a Task entity with the following fields:
- id (long, auto-generated)
- title (String, required)
- description (String, required)
- completed (Boolean, required)
- createdAt (Date, auto timestamped)
- updatedAt (Date, auto timestamped)

Model uses JPA/Hibernate, Jakarta Persistence, Lombok for boilerplate, and validation annotations.

## How To Run
Using Maven: mvn spring-boot:run

## Configuration
Database and other settings are read from `src/main/resources/application.properties`.

Example (application.properties):
- spring.application.name=RestAPI
- spring.datasource.url=jdbc:postgresql://localhost:5432/tasks
- spring.datasource.username=postgres
- spring.datasource.password=1234
- spring.jpa.hibernate.ddl-auto=update

## API Endpoints
The controller uses `/api/tasks` as base path:

- GET /api/tasks
    - List all tasks

- GET /api/tasks/{id}
    - Get task by id

- POST /api/tasks
    - Create a new task
    - Body (application/json):
        {
            "title": "My task",
            "description": "Details",
            "completed": false
        }

- PUT /api/tasks/{id}
    - Update existing task
    - Body (application/json): same shape as POST

- DELETE /api/tasks/{id}
    - Delete task by id

## Example cURL
Create:
curl -X POST http://localhost:8080/api/tasks \
    -H "Content-Type: application/json" \
    -d '{"title":"Test","description":"Desc","completed":false}'

## Notes    
- Ensure Lombok annotation processing is enabled in your build/IDE.
- Timestamps are managed by Hibernate annotations (`@CreationTimestamp`, `@UpdateTimestamp`).
- Adjust package names, base API path, and DB settings as needed.
