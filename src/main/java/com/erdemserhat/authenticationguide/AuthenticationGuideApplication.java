package com.erdemserhat.authenticationguide;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuthenticationGuideApplication {
    // Set the system property to prefer IPv4 stack

    public static void main(String[] args) {
        SpringApplication.run(AuthenticationGuideApplication.class, args);
    }

}
