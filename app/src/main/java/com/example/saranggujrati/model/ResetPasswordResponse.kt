package com.example.saranggujrati.model

data class ResetPasswordResponse(
    val `data`: ResetPasswordData,
    val message: String,
    val status: Boolean
)