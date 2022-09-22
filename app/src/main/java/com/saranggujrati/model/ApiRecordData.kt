package com.saranggujrati.model

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

class ApiRecordData {
    @SerializedName("found")
    @Expose
    var found: Boolean? = null

    @SerializedName("records")
    @Expose
    var records: Int? = null

    @SerializedName("id")
    @Expose
    val id: Int? = null

    @SerializedName("name")
    @Expose
    val name: String? = null
}