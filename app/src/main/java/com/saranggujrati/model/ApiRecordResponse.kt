package com.saranggujrati.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ApiRecordResponse(
    @SerializedName("data")
    @Expose
    val data: List<ApiRecordData>,
    val message: String,
    val status: Boolean
)