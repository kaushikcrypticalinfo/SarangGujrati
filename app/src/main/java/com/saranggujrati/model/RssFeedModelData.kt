package com.saranggujrati.model

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

class RssFeedModelData {
    @SerializedName("title")
    @Expose
    var title: String = ""

    @SerializedName("description")
    @Expose
    var description: String = ""

    @SerializedName("link")
    @Expose
    var link: String = ""

    @SerializedName("cat")
    @Expose
    var cat: String = ""

    @SerializedName("subcat")
    @Expose
    var subcat: String = ""

    @SerializedName("image")
    @Expose
    var image: String = ""

    @SerializedName("publish_date")
    @Expose
    var publishDate: String = ""

  @SerializedName("feed_type")
    @Expose
    var feedType: String = ""

    var isBanner:Boolean = false

    var isAddView:Boolean = false
}