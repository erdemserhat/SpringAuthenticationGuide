package com.erdemserhat.authenticationguide.service

import com.erdemserhat.authenticationguide.data.User
import com.erdemserhat.authenticationguide.repository.UserRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.*
import java.util.function.Supplier

@Configuration
class ApplicationConfiguration(private val userRepository: UserRepository) {
    @Bean
    fun userDetailsService(): UserDetailsService {
        return UserDetailsService { username: String? ->
            // Check if username is null
            if (username.isNullOrEmpty()) {
                throw UsernameNotFoundException("Username cannot be null or empty")
            }

            // Retrieve user from repository
            val userOptional: Optional<User?>? = userRepository.findByEmail(username)

            // Use safe call or check if userOptional is null
            userOptional?.let {
                it.orElseThrow {
                    UsernameNotFoundException("User not found")
                }
            } ?: throw UsernameNotFoundException("User not found")
        }
    }

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    @Throws(Exception::class)
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager {
        return config.authenticationManager
    }

    @Bean
    fun authenticationProvider(): AuthenticationProvider {
        val authProvider = DaoAuthenticationProvider()

        authProvider.setUserDetailsService(userDetailsService())
        authProvider.setPasswordEncoder(passwordEncoder())

        return authProvider
    }
}