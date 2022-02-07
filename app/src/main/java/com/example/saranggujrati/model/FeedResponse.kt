package com.example.saranggujrati.model

data class FeedResponse(
    val `data`: List<CategoryDataModel>,
    val message: String,
    val status: Boolean
)