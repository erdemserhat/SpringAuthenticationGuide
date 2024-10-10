package com.erdemserhat.authenticationguide.controller;


import com.erdemserhat.authenticationguide.data.User;
import com.erdemserhat.authenticationguide.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RequestMapping("/users")
@RestController
public class UserController {
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<String> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User currentUser = (User) authentication.getPrincipal();

        return ResponseEntity.ok("currentUser");
    }

    @GetMapping("/")
    public ResponseEntity<List<User>> allUsers() {
        logger.info("Users fetched successfully");
        List <User> users = userService.allUsers();
        users.forEach(System.out::println);
        logger.info(users.toString());  // Log the entire list


        return ResponseEntity.ok(users);
    }
}