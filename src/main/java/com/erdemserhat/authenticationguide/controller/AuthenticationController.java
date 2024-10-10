package com.erdemserhat.authenticationguide.controller;

import com.erdemserhat.authenticationguide.data.User;
import com.erdemserhat.authenticationguide.dto.LoginResponse;
import com.erdemserhat.authenticationguide.dto.LoginUserDto;
import com.erdemserhat.authenticationguide.dto.RegisterUserDto;
import com.erdemserhat.authenticationguide.service.AuthenticationService;
import com.erdemserhat.authenticationguide.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);


    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto, HttpServletRequest request) {
        //TODO("IMPLEMENT A LOGIC WHICH CONVERTS TO IPV6 TO IPV4")
        String clientIpAddress = extractClientIpAddress(request);
        logger.info("Authentication attempt from IP: {}", clientIpAddress);
        logger.info("credentials checking...");
        //TODO("VALIDATE THE DATA BEFORE PROCESSIONING")

        User authenticatedUser = authenticationService.authenticate(loginUserDto);


        logger.info("{} successfully logged in...", authenticatedUser.getEmail());

        String jwtToken = jwtService.generateToken(authenticatedUser,"exampleRole");

        LoginResponse loginResponse = new LoginResponse(
                jwtToken,
                jwtService.getExpirationTime()
        );

        return ResponseEntity.ok(loginResponse);
    }

    // Helper method to extract the client's IP address
    private String extractClientIpAddress(HttpServletRequest request) {
        String clientIp;

        // Check X-Forwarded-For header for the real client IP (useful if behind a proxy)
        String xForwardedFor = request.getHeader("X-Forwarded-For");

        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            clientIp = xForwardedFor.split(",")[0];  // The first IP in X-Forwarded-For is the client's real IP
        } else {
            clientIp = request.getRemoteAddr();  // Fall back to the direct remote address
        }

        return clientIp;
    }
}

