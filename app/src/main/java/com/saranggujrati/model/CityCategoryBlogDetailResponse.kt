package com.saranggujrati.model

data class CityCategoryBlogDetailResponse(
    val `data`: List<CityCategoryBlogDetailData>,
    val message: String,
    val status: Boolean
)