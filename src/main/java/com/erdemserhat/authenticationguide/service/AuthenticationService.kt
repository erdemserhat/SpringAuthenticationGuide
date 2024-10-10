package com.erdemserhat.authenticationguide.service

import com.erdemserhat.authenticationguide.data.User
import com.erdemserhat.authenticationguide.dto.LoginUserDto
import com.erdemserhat.authenticationguide.dto.RegisterUserDto
import com.erdemserhat.authenticationguide.repository.UserRepository

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    private val userRepository: UserRepository,
    private val authenticationManager: AuthenticationManager,
    private val passwordEncoder: PasswordEncoder
) {
    fun signup(input: RegisterUserDto): User {
        val user = User()
        //TODO("CHECK THE DATA BEFORE TRANSACTION")
        user.fullName = input.fullName
        user.email = input.email
        user.password = passwordEncoder.encode(input.password)

        return userRepository.save(user)
    }

    fun authenticate(input: LoginUserDto): User? {
        // Authenticate the user using the provided email and password
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                input.email,
                input.password
            )
        )

        // Retrieve the user from the repository and handle the case where the user is not found
        return userRepository.findByEmail(input.email)
            ?.orElseThrow { Exception("User with email ${input.email} not found") }
    }
}