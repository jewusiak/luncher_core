# Luncher Core

Luncher Core is the central backend service for the platform—a modern, scalable system for managing Restaurants' Menus (especially lunch menus) - assets like places, users, content, and user generated content. Built with Java 21 and Spring Boot 3, it provides robust APIs for authentication, user management, place management, and content arrangement, making it ideal for powering restaurant, venue, or content-driven applications.

## Key Features

- **Modern Java & Spring Boot 3**: Leverages the latest Java and Spring technologies for performance and maintainability.
- **Comprehensive REST API**: Endpoints for authentication, user management, places, content, and assets.
- **Role-Based Access Control**: Fine-grained permissions for users, managers, and system moderators.
- **Content Management**: Flexible arrangement and management of pages and sections.
- **Asset Management**: Upload and associate images/assets with places and content.
- **Full-Text Search**: Integrated with Hibernate Search and Elasticsearch for fast, relevant queries.
- **OpenAPI/Swagger Documentation**: Interactive API docs for easy integration and testing.
- **Testable & Containerized**: Docker support for local development and testing with PostgreSQL and Elasticsearch.

## Tech Stack

- **Java 21**
- **Spring Boot 3.2** (Web, Data JPA, Security, Validation)
- **Hibernate & Hibernate Search**: Advanced full-text search capabilities with Elasticsearch integration.
- **Elasticsearch**: Optional backend for high-performance search and analytics.
- **PostgreSQL/MySQL/H2**: Flexible database options for different runtime profiles.
- **JWT Authentication**: Secure token-based authentication.
- **Lombok, MapStruct**: Tools for clean code and efficient object mapping.
- **Cucumber & JUnit 5**: Comprehensive testing framework for behavior-driven development.
- **Docker**: Containerization for consistent development and deployment environments.

## Main API Modules

- **Authentication**: Login, logout, registration, password reset (`/auth`)
- **User Management**: CRUD, search, roles, permissions (`/users`)
- **Place Management**: CRUD, search, image management (`/place`)
- **Content Management**: Page/section arrangement, asset linking
- **Asset Management**: Upload, fetch, delete images/assets

API documentation is available via Swagger UI when running locally.
