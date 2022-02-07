package com.example.saranggujrati.model

data class SignUpData(
    val api_token: String,
    val created_at: String,
    val email: String,
    val gender: Any,
    val id: Int,
    val isNewUser: Boolean,
    val name: String,
    val otp: String,
    val phone: String,
    val photo: String,
    val type: String,
    val updated_at: String
)