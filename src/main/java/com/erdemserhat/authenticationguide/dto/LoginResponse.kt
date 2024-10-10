package com.erdemserhat.authenticationguide.dto

data class LoginResponse(
    var token:String,
    var expiresIn:Long
)