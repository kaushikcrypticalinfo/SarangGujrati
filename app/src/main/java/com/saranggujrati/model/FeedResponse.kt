package com.saranggujrati.model

data class FeedResponse(
    val `data`: List<RssFeedModelData>,
    val message: String,
    val status: Boolean
)