package com.saranggujrati.model

data class NewsPaperListResponse(
    val `data`: NewsPaperDataMain,
    val message: String,
    val status: Boolean
)