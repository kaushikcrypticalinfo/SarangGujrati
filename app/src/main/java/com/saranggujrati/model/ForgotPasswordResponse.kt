package com.saranggujrati.model

data class ForgotPasswordResponse(
    val `data`: ForgotPasswordData,
    val message: String,
    val status: Boolean
)