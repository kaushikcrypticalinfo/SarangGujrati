package com.example.saranggujrati.model

data class CategoryListResponse(
    val `data`: List<TopCitiesData>,
    val message: String,
    val status: Boolean
)