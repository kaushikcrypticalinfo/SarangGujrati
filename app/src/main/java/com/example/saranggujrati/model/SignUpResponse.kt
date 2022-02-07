package com.example.saranggujrati.model

data class SignUpResponse(
    val `data`: SignUpData,
    val message: String,
    val status: Boolean
)