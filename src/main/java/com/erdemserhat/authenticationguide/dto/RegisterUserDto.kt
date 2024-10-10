package com.erdemserhat.authenticationguide.dto

data class RegisterUserDto(
    val email: String,
    val password: String,
    val fullName: String,
)