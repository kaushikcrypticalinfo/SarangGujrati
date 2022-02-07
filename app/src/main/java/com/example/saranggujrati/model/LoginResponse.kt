package com.example.saranggujrati.model

data class LoginResponse(
    val `data`: LoginData,
    val message: String,
    val status: Boolean
)