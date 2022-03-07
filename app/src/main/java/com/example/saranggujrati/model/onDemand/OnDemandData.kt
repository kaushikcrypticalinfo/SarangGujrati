package com.example.saranggujrati.model.onDemand

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

class OnDemandData {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("thumb_image")
    @Expose
    var thumbImage: Any? = null

    @SerializedName("image")
    @Expose
    var image: String? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("url")
    @Expose
    var url: String? = null

    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null

    @SerializedName("deleted_at")
    @Expose
    var deletedAt: Any? = null
}