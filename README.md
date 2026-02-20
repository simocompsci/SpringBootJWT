# JWT Authentication System

A clean, production-ready JWT authentication system built with Spring Boot 3.5.10.

## Quick Start

1. **Configure Database**: Update `application.properties` with your PostgreSQL credentials
2. **Run Application**: `mvn spring-boot:run`
3. **Register User**: POST to `/auth/register`
4. **Login**: POST to `/auth/login` to get JWT token
5. **Access Protected Endpoints**: Include token in `Authorization: Bearer <token>` header

## Authentication Flow

1. **Client Request**: Client sends HTTP request with credentials (login) or user data (register)
2. **Authentication**: Spring Security validates credentials using `AuthenticationManager`
3. **Token Generation**: `JwtService` generates JWT token with user details
4. **Token Response**: Server returns JWT token to client
5. **Protected Requests**: Client includes token in `Authorization: Bearer <token>` header
6. **Filter Processing**: `JwtAuthFilter` intercepts request:
   - Extracts token from Authorization header
   - Validates token using `JwtService`
   - Loads user details using `UserDetailsService`
   - Sets authentication in `SecurityContextHolder`
7. **Authorization**: Spring Security checks user roles and permissions
8. **Controller Execution**: Request reaches controller if authenticated and authorized
9. **Response**: Controller processes request and returns JSON response

## Key Components

- **JwtAuthFilter**: Validates JWT tokens on each request
- **JwtService**: Handles token generation, validation, and extraction
- **UserInfoService**: Implements `UserDetailsService` for user loading
- **SecurityConfig**: Configures Spring Security with JWT filter
- **GlobalExceptionHandler**: Provides consistent error responses

## Documentation

See [API_DOCUMENTATION.md](./API_DOCUMENTATION.md) for complete API documentation.

## Security Features

- ✅ BCrypt password encoding
- ✅ JWT token-based authentication
- ✅ Role-based access control (RBAC)
- ✅ Stateless session management
- ✅ Token expiration handling
- ✅ Input validation
- ✅ Global exception handling