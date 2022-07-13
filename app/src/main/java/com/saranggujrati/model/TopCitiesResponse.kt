package com.saranggujrati.model

data class TopCitiesResponse(
    val `data`: List<TopCitiesData>,
    val message: String,
    val status: Boolean
)