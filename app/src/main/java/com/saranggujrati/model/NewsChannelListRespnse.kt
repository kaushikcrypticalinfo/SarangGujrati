package com.saranggujrati.model

data class NewsChannelListRespnse(
    val `data`: NewsDataMain,
    val message: String,
    val status: Boolean
)