package com.saranggujrati.model

data class GetEditProfileResponse(
    val `data`: GetEditProfileData,
    val message: String,
    val status: Boolean
)