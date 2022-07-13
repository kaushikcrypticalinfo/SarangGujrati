package com.saranggujrati.model.rssFeed

import com.saranggujrati.utils.*
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Root

@Root(name = THUMBNAIL, strict = false)
data class Thumbnail(
    @field:Attribute(name = URL)
    var thumbnailUrl: String? = null
)