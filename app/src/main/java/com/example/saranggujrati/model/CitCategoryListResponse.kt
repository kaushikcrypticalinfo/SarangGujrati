package com.example.saranggujrati.model

data class CitCategoryListResponse(
    val `data`: List<CityCategoryListData>,
    val message: String,
    val status: Boolean
)