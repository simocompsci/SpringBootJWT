# JWT Authentication API Documentation

## Overview
This is a clean, production-ready JWT authentication system built with Spring Boot 3.5.10. It provides secure user registration, login, and role-based access control.

## Features
- ✅ User Registration with email validation
- ✅ User Login with JWT token generation
- ✅ JWT Token-based authentication
- ✅ Role-based access control (USER, ADMIN)
- ✅ Secure password encoding (BCrypt)
- ✅ Global exception handling
- ✅ Proper error responses
- ✅ Stateless session management

## API Endpoints

### Public Endpoints (No Authentication Required)

#### 1. Welcome Endpoint
```
GET /auth/welcome
```
**Response:**
```json
"Welcome! This endpoint is not secure."
```

#### 2. User Registration
```
POST /auth/register
Content-Type: application/json
```
**Request Body:**
```json
{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "password": "securePassword123",
  "roles": "ROLE_USER"
}
```
**Note:** If `roles` is not provided, it defaults to `ROLE_USER`.

**Response (201 Created):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "message": "User registered successfully",
  "username": "john.doe@example.com"
}
```

#### 3. User Login
```
POST /auth/login
Content-Type: application/json
```
**Request Body:**
```json
{
  "username": "john.doe@example.com",
  "password": "securePassword123"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "message": "Authentication successful",
  "username": "john.doe@example.com"
}
```

### Protected Endpoints (Authentication Required)

#### 4. User Profile (USER or ADMIN role)
```
GET /auth/user/profile
Authorization: Bearer <token>
```
**Response (200 OK):**
```json
"User profile endpoint - Access granted"
```

#### 5. Admin Users List (ADMIN role only)
```
GET /auth/admin/users
Authorization: Bearer <token>
```
**Response (200 OK):**
```json
"Admin endpoint - Access granted"
```

## Error Responses

All errors follow a consistent format:

```json
{
  "timestamp": "2026-02-20T10:30:00",
  "status": 401,
  "error": "Invalid Credentials",
  "message": "Invalid username or password",
  "path": "/auth/login"
}
```

### Common HTTP Status Codes
- `200 OK` - Success
- `201 Created` - Resource created successfully
- `400 Bad Request` - Invalid request data
- `401 Unauthorized` - Invalid credentials or missing/invalid token
- `403 Forbidden` - Insufficient permissions
- `404 Not Found` - User not found
- `500 Internal Server Error` - Server error

## Authentication Flow

1. **Registration/Login**: User provides credentials
2. **Token Generation**: Server validates credentials and generates JWT token
3. **Token Usage**: Client includes token in `Authorization` header: `Bearer <token>`
4. **Token Validation**: Filter validates token on each request
5. **Access Control**: Spring Security checks user roles and permissions

## JWT Token Structure

The JWT token contains:
- **Subject**: User email (username)
- **Issued At**: Token creation timestamp
- **Expiration**: Token expiration timestamp (default: 30 minutes)
- **Claims**: User authorities/roles

## Security Configuration

- **Password Encoding**: BCryptPasswordEncoder
- **Session Management**: STATELESS (no server-side sessions)
- **CSRF**: Disabled (stateless API)
- **Token Expiration**: Configurable via `jwt.expiration` in `application.properties`

## Configuration

### application.properties
```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/JWTDB
spring.datasource.username=postgres
spring.datasource.password=password

# JWT Configuration
jwt.secret=5367566859703373367639792F423F452848284D6251655468576D5A71347437
jwt.expiration=1800000  # 30 minutes in milliseconds
```

**⚠️ Important**: Change the `jwt.secret` in production! Generate a secure random base64-encoded key.

## Testing with cURL

### Register a User
```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "password123",
    "roles": "ROLE_USER"
  }'
```

### Login
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john@example.com",
    "password": "password123"
  }'
```

### Access Protected Endpoint
```bash
curl -X GET http://localhost:8080/auth/user/profile \
  -H "Authorization: Bearer <your-token-here>"
```

## Project Structure

```
src/main/java/com/Auth/JWT/
├── Config/
│   └── SecurityConfig.java          # Spring Security configuration
├── Controller/
│   └── UserController.java          # REST API endpoints
├── DTO/
│   ├── AuthRequest.java             # Login request DTO
│   ├── AuthResponse.java            # Authentication response DTO
│   ├── RegisterRequest.java         # Registration request DTO
│   ├── UserResponse.java            # User data response DTO
│   └── ErrorResponse.java           # Error response DTO
├── Entity/
│   └── UserInfo.java                # User entity
├── Exception/
│   └── GlobalExceptionHandler.java  # Global exception handling
├── Filter/
│   └── JwtAuthFilter.java          # JWT authentication filter
├── Respository/
│   └── UserInfoRepository.java      # User repository
└── Service/
    ├── JwtService.java              # JWT token operations
    ├── UserInfoDetails.java         # UserDetails implementation
    └── UserInfoService.java         # User service & UserDetailsService
```

## Best Practices Implemented

1. ✅ **Separation of Concerns**: Clear separation between controllers, services, and repositories
2. ✅ **DTO Pattern**: Request/Response DTOs for data transfer
3. ✅ **Exception Handling**: Global exception handler for consistent error responses
4. ✅ **Security**: Proper password encoding, token validation, and role-based access
5. ✅ **Validation**: Input validation using Jakarta Validation
6. ✅ **Configuration Externalization**: JWT secret and expiration in properties file
7. ✅ **Clean Code**: Proper naming, structure, and documentation

## Improvements Made

1. Fixed `UserInfoDetails.getPassword()` implementation
2. Added `findByEmail()` method to repository
3. Externalized JWT secret to configuration
4. Improved exception handling in filter and service
5. Added proper DTOs for all responses
6. Implemented global exception handler
7. Added input validation
8. Improved error messages and responses
9. Updated endpoints to follow RESTful conventions
10. Enhanced security configuration
