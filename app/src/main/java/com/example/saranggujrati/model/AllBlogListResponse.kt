package com.example.saranggujrati.model

data class AllBlogListResponse(
    val `data`: List<BlogData>,
    val message: String,
    val status: Boolean
)