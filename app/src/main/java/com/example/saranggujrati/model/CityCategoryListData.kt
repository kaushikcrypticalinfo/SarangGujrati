package com.example.saranggujrati.model

data class CityCategoryListData(
    val child: List<CityCatageoryChild>,
    val color: String,
    val created_at: String,
    val deleted_at: Any,
    val id: Int,
    val image: String,
    val index: Int,
    val is_featured: String,
    val name: String,
    val order: Any,
    val parent_id: String,
    val slug: String,
    val status: String,
    val updated_at: String
)