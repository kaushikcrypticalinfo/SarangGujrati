package com.example.saranggujrati.model

data class SocialLoginResponse(
    val `data`: SocialLoginData,
    val message: String,
    val status: Boolean
)