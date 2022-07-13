package com.saranggujrati.model

data class ResetPasswordData(
    val cpassword: String,
    val email: String,
    val otp: String,
    val password: String
)