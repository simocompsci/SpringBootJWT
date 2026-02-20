package com.Auth.JWT.Controller;

import com.Auth.JWT.DTO.AuthRequest;
import com.Auth.JWT.DTO.AuthResponse;
import com.Auth.JWT.DTO.RegisterRequest;
import com.Auth.JWT.Entity.UserInfo;
import com.Auth.JWT.Service.JwtService;
import com.Auth.JWT.Service.UserInfoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserInfoService userInfoService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @GetMapping("/welcome")
    public ResponseEntity<String> welcome() {
        return ResponseEntity.ok("Welcome! This endpoint is not secure.");
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        UserInfo userInfo = new UserInfo();
        userInfo.setName(request.getName());
        userInfo.setEmail(request.getEmail());
        userInfo.setPassword(request.getPassword());
        userInfo.setRoles(request.getRoles() != null ? request.getRoles() : "ROLE_USER");
        
        userInfoService.addUser(userInfo);
        
        UserDetails userDetails = userInfoService.loadUserByUsername(request.getEmail());
        String token = jwtService.generateToken(userDetails);
        
        AuthResponse response = AuthResponse.builder()
                .token(token)
                .message("User registered successfully")
                .username(request.getEmail())
                .build();
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateAndGetToken(@Valid @RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()
                    )
            );

            if (authentication.isAuthenticated()) {
                UserDetails userDetails = userInfoService.loadUserByUsername(authRequest.getUsername());
                String token = jwtService.generateToken(userDetails);
                
                AuthResponse response = AuthResponse.builder()
                        .token(token)
                        .message("Authentication successful")
                        .username(authRequest.getUsername())
                        .build();
                
                return ResponseEntity.ok(response);
            } else {
                throw new BadCredentialsException("Invalid credentials");
            }
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid username or password", e);
        }
    }

    @GetMapping("/user/profile")
    public ResponseEntity<String> getUserProfile() {
        return ResponseEntity.ok("User profile endpoint - Access granted");
    }

    @GetMapping("/admin/users")
    public ResponseEntity<String> getAllUsers() {
        return ResponseEntity.ok("Admin endpoint - Access granted");
    }
}

