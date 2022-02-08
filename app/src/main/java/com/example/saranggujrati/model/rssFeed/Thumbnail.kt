package com.example.saranggujrati.model.rssFeed

import com.example.saranggujrati.utils.*
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Root

@Root(name = THUMBNAIL, strict = false)
data class Thumbnail(
    @field:Attribute(name = THUMBNAIL_URL)
    var thumbnailUrl: String? = null
)